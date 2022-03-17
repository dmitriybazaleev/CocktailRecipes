package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.baza.cocktailrecipe.databinding.FragmentSearchByNameBinding
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchByNameAdapter
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkUiEntitySearch
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.SearchHolder
import com.baza.cocktailrecipe.presentation.module.ui.textChanges
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SearchByNameViewModel
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

class SearchByNameFragment : BaseFragment<FragmentSearchByNameBinding>(),
    SearchHolder.ItemObserver {

    private val rootViewModel by viewModels<SearchViewModel>(
        ownerProducer = { requireParentFragment() }
    )
    private val viewModel by viewModels<SearchByNameViewModel>()

    private val mSearchAdapter = SearchByNameAdapter(this)
    private var mJob: Job? = null

    companion object {

        @JvmStatic
        fun create() = SearchByNameFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchByNameBinding
        get() = FragmentSearchByNameBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStateObserver()
        setUpRecycler()
        initListeners()
        addEventObserver()
    }

    private fun addEventObserver() {
        viewModel.searchEvent
            .onEach { event ->

            }
            .launchIn(lifecycleScope)
    }


    private fun setUpRecycler() {
        binding?.rvSearchByName?.adapter = mSearchAdapter
    }

    private fun addStateObserver() {
        viewModel.searchByNameLiveData.observe(viewLifecycleOwner) { state ->
            updateProgress(state.isShowProgress)
            mSearchAdapter.updateList(state.searchResult)
            updatePlaceholder(state.isShowPlaceholder)
        }
    }

    private fun updateProgress(isShow: Boolean) {
        binding?.pbSearchByName?.isVisible = isShow
    }

    private fun updatePlaceholder(isShow: Boolean) {
        if (binding?.pvSearchByName?.isVisible != isShow) {
            binding?.pvSearchByName?.isVisible = isShow
        }
    }

    override fun onStart() {
        super.onStart()
        addTextWatcherListener()
    }

    private fun addTextWatcherListener() {
        mJob = binding?.etSearchByName?.textChanges()
            ?.debounce(DEFAULT_TEXT_DELAY)
            ?.onEach { input ->
                input?.let { inputNotNull ->
                    if (inputNotNull.isNotEmpty()) {
                        viewModel.onStartSearch(inputNotNull.toString())

                    } else {
                        viewModel.onClearCurrentList()
                    }
                }
            }
            ?.launchIn(lifecycleScope)
    }

    private fun initListeners() {
        binding?.ibSearchClear?.setOnClickListener {
            binding?.etSearchByName?.text?.clear()
        }
    }

    override fun onStop() {
        super.onStop()
        mJob?.cancel()
    }

    override fun onItemClicked(item: DrinkUiEntitySearch) {
        // TODO: 12.03.2022 Replace with your own action!
        Log.d(SearchByNameViewModel.TAG, "item clicked!")
    }
}