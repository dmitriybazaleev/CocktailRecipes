package com.baza.navigation

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.core.view.forEach
import androidx.navigation.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference
import java.util.*

object NavigationUtilsNew {

    private const val TAG = "navigationUtilsNew"

    /**
     * Настраивает {@link BottomNavigationView} для использования с {@link NavController}. Это вызовет
     * {@link #onNavDestinationSelected (MenuItem, NavController)} при выборе пункта меню.
     *
     * @param bottomNav  BottomNavigationView, который должен синхронизироваться с
     *                   изменения в NavController.
     * @param controller Контроллер NavController, который предоставляет главное меню.
     *                   Действия навигации в этом NavController будут отражены в
     *                   выбранный элемент в BottomNavigationView.
     */
    fun setUpWithNavController(
        bottomNavigation: BottomNavigationView,
        navController: NavController
    ) {
        bottomNavigation.setOnItemSelectedListener { menuItem ->
            return@setOnItemSelectedListener if (bottomNavigation.selectedItemId == menuItem.itemId)
                false
            else onNavigationItemSelected(
                navController = navController,
                item = menuItem
            )
        }

        val bottomNavRef = WeakReference(bottomNavigation)

        onDestinationChanged(bottomNavRef.get(), navController)
    }

    private fun onDestinationChanged(
        bottomNav: BottomNavigationView?,
        navController: NavController
    ) {
        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    Log.d(TAG, "destination changed: $destination")
                    if (bottomNav == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }

                    bottomNav.menu.forEach { menuItem ->
                        if (destination.matchDestination(menuItem.itemId)) {
                            menuItem.isChecked = true
                        }
                    }
                }
            })
    }

    private fun onNavigationItemSelected(
        item: MenuItem,
        navController: NavController
    ): Boolean {
        val navOption = NavOptions.Builder()
            .setLaunchSingleTop(true)

        when (navController.currentDestination?.parent?.findNode(item.itemId)) {
            is ActivityNavigator.Destination -> {
                navOption.setEnterAnim(R.anim.nav_default_enter_anim)
                    .setExitAnim(R.anim.nav_default_exit_anim)
                    .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                    .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
            }

            else -> {
                navOption.setEnterAnim(R.animator.nav_default_enter_anim)
                    .setExitAnim(R.animator.nav_default_exit_anim)
                    .setPopEnterAnim(R.animator.nav_default_pop_enter_anim)
                    .setPopExitAnim(R.animator.nav_default_pop_exit_anim)
            }
        }
        if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            navOption.setPopUpTo(
                destinationId = findStartDestination(graph = navController.graph)?.id ?: -1,
                inclusive = false
            )
        }

        return tabNavigated(navController, item, navOption = navOption.build())
    }

    private fun tabNavigated(
        navController: NavController,
        item: MenuItem,
        navOption: NavOptions
    ): Boolean {
        return try {
            val popped = navController.popAllInstances(item.itemId, false)
            if (!popped)
                navController.navigate(item.itemId, null, navOption)

            true

        } catch (e: IllegalArgumentException) {
            false
        }
    }

    /**
     * Находит фактическое начальное место назначения графика, обрабатывая случаи, когда начало графика
     * пункт назначения сам по себе является NavGraph.
     */
    private fun findStartDestination(graph: NavGraph): NavDestination? {
        var startDestination: NavDestination? = graph
        while (startDestination is NavGraph) {
            val parent = startDestination
            startDestination = parent.findNode(parent.startDestinationId)
        }
        return startDestination
    }

    /**
     * Determines whether the given `destId` matches the NavDestination. This handles
     * both the default case (the destination's id matches the given id) and the nested case where
     * the given id is a parent/grandparent/etc of the destination.
     */
    @JvmStatic
    internal fun NavDestination.matchDestination(@IdRes destId: Int): Boolean =
        hierarchy.any { it.id == destId }

    /**
     * Provides a sequence of the NavDestination's hierarchy. The hierarchy starts with this
     * destination itself and is then followed by this destination's [NavDestination.parent], then that
     * graph's parent, and up the hierarchy until you've reached the root navigation graph.
     */
    @JvmStatic
    val NavDestination.hierarchy: Sequence<NavDestination>
        get() = generateSequence(this) { it.parent }
}