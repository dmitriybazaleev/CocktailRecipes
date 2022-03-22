package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.lang.ref.WeakReference

/**
 * A mediator to link a TabLayout with a ViewPager2. The mediator will synchronize the ViewPager2's
 * position with the selected tab when a tab is selected, and the TabLayout's scroll position when
 * the user drags the ViewPager2. TabLayoutMediator will listen to ViewPager2's OnPageChangeCallback
 * to adjust tab when ViewPager2 moves. TabLayoutMediator listens to TabLayout's
 * OnTabSelectedListener to adjust VP2 when tab moves. TabLayoutMediator listens to RecyclerView's
 * AdapterDataObserver to recreate tab content when dataset changes.
 *
 *
 * Establish the link by creating an instance of this class, make sure the ViewPager2 has an
 * adapter and then call [.attach] on it. Instantiating a TabLayoutMediator will only create
 * the mediator object, [.attach] will link the TabLayout and the ViewPager2 together. When
 * creating an instance of this class, you must supply an implementation of [ ] in which you set the text of the tab, and/or perform any styling of the
 * tabs that you require. Changing ViewPager2's adapter will require a [.detach] followed by
 * [.attach] call. Changing the ViewPager2 or TabLayout will require a new instantiation of
 * TabLayoutMediator.
 */
fun ViewPager2.attachToTab(
    tabLayout: TabLayout,
    autoRefresh: Boolean = true,
    smoothScroll: Boolean = true,
    strategyCallback: (tab: TabLayout.Tab, position: Int) -> Unit
) {
    TabMediatorNew(
        tabLayout = tabLayout,
        viewPager = this,
        autoRefresh = autoRefresh,
        smoothScroll = smoothScroll,
        strategyCallback = strategyCallback
    ).attach()
}

fun TabLayout.attachToPager(
    viewPager: ViewPager2,
    autoRefresh: Boolean = true,
    smoothScroll: Boolean = true,
    strategyCallback: (tab: TabLayout.Tab, position: Int) -> Unit
) {
    TabMediatorNew(
        tabLayout = this,
        viewPager = viewPager,
        autoRefresh = autoRefresh,
        smoothScroll = smoothScroll,
        strategyCallback = strategyCallback
    ).attach()
}

class TabMediatorNew constructor(
    private val tabLayout: TabLayout,
    private val viewPager: ViewPager2,
    private val autoRefresh: Boolean = true,
    private val smoothScroll: Boolean = true,
    private val strategyCallback: (tab: TabLayout.Tab, position: Int) -> Unit
) {
    private var adapter: RecyclerView.Adapter<*>? = null

    /**
     * Returns whether the [TabLayout] and the [ViewPager2] are linked together.
     */
    var isAttached = false
        private set

    private var onPageChangeCallback: TabLayoutOnPageChangeCallback? = null
    private var onTabSelectedListener: OnTabSelectedListener? = null
    private var pagerAdapterObserver: AdapterDataObserver? = null


    /**
     * Link the TabLayout and the ViewPager2 together. Must be called after ViewPager2 has an adapter
     * set. To be called on a new instance of TabLayoutMediator or if the ViewPager2's adapter
     * changes.
     *
     * @throws IllegalStateException If the mediator is already attached, or the ViewPager2 has no
     * adapter.
     */
    fun attach() {
        check(!isAttached) {
            "TabLayoutMediator is already attached"
        }
        adapter = viewPager.adapter
        checkNotNull(adapter) { "TabLayoutMediator attached before ViewPager2 has an " + "adapter" }
        isAttached = true

        // Add our custom OnPageChangeCallback to the ViewPager
        onPageChangeCallback = TabLayoutOnPageChangeCallback(tabLayout)
        viewPager.registerOnPageChangeCallback(onPageChangeCallback ?: return)

        // Now we'll add a tab selected listener to set ViewPager's current item
        onTabSelectedListener = ViewPagerOnTabSelectedListener(
            viewPager,
            smoothScroll
        )
        tabLayout.addOnTabSelectedListener(onTabSelectedListener ?: return)

        // Now we'll populate ourselves from the pager adapter, adding an observer if
        // autoRefresh is enabled
        if (autoRefresh) {
            // Register our observer on the new adapter
            pagerAdapterObserver = PagerAdapterObserver()
            adapter?.registerAdapterDataObserver(pagerAdapterObserver ?: return)
        }
        populateTabsFromPagerAdapter()

        // Now update the scroll position to match the ViewPager's current item
        tabLayout.setScrollPosition(viewPager.currentItem, 0f, true)
    }

    /**
     * Unlink the TabLayout and the ViewPager. To be called on a stale TabLayoutMediator if a new one
     * is instantiated, to prevent holding on to a view that should be garbage collected. Also to be
     * called before [.attach] when a ViewPager2's adapter is changed.
     */
    fun detach() {
        if (autoRefresh && adapter != null) {
            adapter?.unregisterAdapterDataObserver(pagerAdapterObserver ?: return)
            pagerAdapterObserver = null
        }
        tabLayout.removeOnTabSelectedListener(onTabSelectedListener ?: return)
        viewPager.unregisterOnPageChangeCallback(onPageChangeCallback ?: return)
        onTabSelectedListener = null
        onPageChangeCallback = null
        adapter = null
        isAttached = false
    }

    fun populateTabsFromPagerAdapter() {
        tabLayout.removeAllTabs()

        adapter?.let { adapterNotNull ->
            val adapterCount = adapterNotNull.itemCount
            for (i in 0 until adapterCount) {
                val tab = tabLayout.newTab()
                strategyCallback.invoke(tab, i)
                tabLayout.addTab(tab, false)
            }
            // Make sure we reflect the currently set ViewPager item
            if (adapterCount > 0) {
                val lastItem = tabLayout.tabCount - 1
                val currItem = viewPager.currentItem.coerceAtMost(lastItem)
                if (currItem != tabLayout.selectedTabPosition) {
                    tabLayout.selectTab(tabLayout.getTabAt(currItem))
                }
            }
        }
    }

    /**
     * A [ViewPager2.OnPageChangeCallback] class which contains the necessary calls back to the
     * provided [TabLayout] so that the tab position is kept in sync.
     *
     *
     * This class stores the provided TabLayout weakly, meaning that you can use [ ][ViewPager2.registerOnPageChangeCallback] without removing the
     * callback and not cause a leak.
     */
    private class TabLayoutOnPageChangeCallback(
        tabLayout: TabLayout
    ) : OnPageChangeCallback() {

        private val tabLayoutRef: WeakReference<TabLayout> = WeakReference(tabLayout)
        private var previousScrollState = 0
        private var scrollState = 0

        override fun onPageScrollStateChanged(state: Int) {
            previousScrollState = scrollState
            scrollState = state
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            val tabLayout = tabLayoutRef.get()
            if (tabLayout != null) {
                // Only update the text selection if we're not settling, or we are settling after
                // being dragged
                val updateText =
                    scrollState != ViewPager2.SCROLL_STATE_SETTLING
                            || previousScrollState == ViewPager2.SCROLL_STATE_DRAGGING
                // Update the indicator if we're not settling after being idle. This is caused
                // from a setCurrentItem() call and will be handled by an animation from
                // onPageSelected() instead.
                val updateIndicator =
                    !(scrollState == ViewPager2.SCROLL_STATE_SETTLING
                            && previousScrollState == ViewPager2.SCROLL_STATE_IDLE)
                tabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator)
            }
        }

        override fun onPageSelected(position: Int) {
            val tabLayout = tabLayoutRef.get()
            if (tabLayout != null && tabLayout.selectedTabPosition != position && position < tabLayout.tabCount) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                val updateIndicator = (scrollState == ViewPager2.SCROLL_STATE_IDLE
                        || (scrollState == ViewPager2.SCROLL_STATE_SETTLING
                        && previousScrollState == ViewPager2.SCROLL_STATE_IDLE))
                tabLayout.selectTab(tabLayout.getTabAt(position), updateIndicator)
            }
        }

        fun reset() {
            scrollState = ViewPager2.SCROLL_STATE_IDLE
            previousScrollState = scrollState
        }

        init {
            reset()
        }
    }

    /**
     * A [TabLayout.OnTabSelectedListener] class which contains the necessary calls back to the
     * provided [ViewPager2] so that the tab position is kept in sync.
     */
    private class ViewPagerOnTabSelectedListener(
        private val viewPager: ViewPager2,
        private val smoothScroll: Boolean
    ) : OnTabSelectedListener {

        override fun onTabSelected(tab: TabLayout.Tab) {
            viewPager.setCurrentItem(tab.position, smoothScroll)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            // Nothing to do
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            // Nothing to do
        }
    }

    private inner class PagerAdapterObserver : AdapterDataObserver() {
        override fun onChanged() {
            populateTabsFromPagerAdapter()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            populateTabsFromPagerAdapter()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            populateTabsFromPagerAdapter()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            populateTabsFromPagerAdapter()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            populateTabsFromPagerAdapter()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            populateTabsFromPagerAdapter()
        }
    }
}