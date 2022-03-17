package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.baza.cocktailrecipe.databinding.FragmentSearchByIngredientBinding
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchIngredientAdapter
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.IngredientUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.IngredientHolder
import com.baza.cocktailrecipe.presentation.module.ui.textChanges
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SearchByIngredientViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class SearchByIngredientFragment : BaseFragment<FragmentSearchByIngredientBinding>(),
    IngredientHolder.ItemObserver {

    companion object {
        const val TAG = "searchByIngredient"

        @JvmStatic
        fun create() = SearchByIngredientFragment()
    }

    private val mAdapter = SearchIngredientAdapter(this)
    private val rootViewModel by viewModels<SearchByIngredientViewModel>(
        ownerProducer = { requireParentFragment() }
    )
    private val viewModel by viewModels<SearchByIngredientViewModel>()

    private var mInputJob: Job? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchByIngredientBinding
        get() = FragmentSearchByIngredientBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWithRecycler()
        addStateObserver()
    }

    private fun addStateObserver() {
        viewModel.searchLiveData.observe(viewLifecycleOwner) { state ->
            updateProgressState(state.isShowProgress)
            updatePlaceholderState(state.isShowPlaceholder)
        }
    }

    private fun updatePlaceholderState(isShow: Boolean) {
        binding?.pvSearchByIngredient?.isVisible = isShow
    }

    private fun updateProgressState(isShow: Boolean) {
        binding?.pbSearchByIngredient?.isVisible = isShow
    }

    override fun onStart() {
        super.onStart()
        addTextWatcher()
    }

    private fun addTextWatcher() {
        mInputJob = binding?.etSearchByIngredient?.textChanges()
            ?.debounce(DEFAULT_TEXT_DELAY)
            ?.onEach { input ->
                input?.let { inputNotNull ->
                    if (inputNotNull.isNotEmpty()) {
                        // Ищем ингредиенты
                        viewModel.onSearch(inputNotNull.toString())

                    } else {
                        viewModel.onClearResponse()
                    }
                }
            }
            ?.launchIn(lifecycleScope)
    }

    override fun onStop() {
        super.onStop()
        mInputJob?.cancel()
    }

    private fun setUpWithRecycler() {
        binding?.rvSearchByIngredient?.adapter = mAdapter

    }

    override fun onItemClicked(item: IngredientUiEntity) {
        // TODO: 17.03.2022 Replace with your own action!
        Log.d(TAG, "search item clicked: $item")
    }
}