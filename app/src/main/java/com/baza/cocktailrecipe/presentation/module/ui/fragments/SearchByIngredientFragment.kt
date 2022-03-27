package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.os.Bundle
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
    IngredientHolder.IngredientItemObserver {

    private val viewModel by viewModels<SearchByIngredientViewModel>()
    private var mJobInput: Job? = null

    private var mAdapter: SearchIngredientAdapter = SearchIngredientAdapter(
        ingredientItemObserver = this
    )

    companion object {

        @JvmStatic
        fun create() = SearchByIngredientFragment()
    }

    override val bindingInflater: (
        LayoutInflater, ViewGroup?, Boolean
    ) -> FragmentSearchByIngredientBinding
        get() = FragmentSearchByIngredientBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStateObserver()
        initEventObserver()
        setUpWithRecycler()
        addListeners()
    }

    private fun addListeners() {
        binding?.ibSearchClear?.setOnClickListener {
            binding?.etSearchByIngredient?.text?.clear()
        }
    }

    private fun setUpWithRecycler() {
        binding?.rvSearchByIngredient?.adapter = mAdapter
    }

    override fun onStart() {
        super.onStart()
        initTextWatcher()
    }

    override fun onStop() {
        super.onStop()
        mJobInput?.cancel()
    }

    private fun initTextWatcher() {
        mJobInput = binding?.etSearchByIngredient?.textChanges()
            ?.debounce(DEFAULT_TEXT_DELAY)
            ?.onEach { input ->
                input?.let { charSequence ->
                    if (charSequence.isNotEmpty()) {
                        viewModel.onStartSearch(charSequence.toString())

                    } else {
                        // Clear current list
                    }
                }
            }
            ?.launchIn(lifecycleScope)
    }

    private fun updateProgressState(isShow: Boolean) {
        binding?.pbSearchByIngredient?.isVisible = isShow
    }

    private fun updatePlaceholderState(isShow: Boolean) {
        binding?.pvSearchByIngredient?.isVisible = isShow
    }

    private fun initEventObserver() {
        viewModel.ingredientEvent
            .onEach { event ->

            }
            .launchIn(lifecycleScope)
    }

    private fun initStateObserver() {
        viewModel.searchIngredientLiveData.observe(viewLifecycleOwner) { state ->
            updatePlaceholderState(state.isShowPlaceholder)
            updateProgressState(state.isShowProgress)
        }
    }

    override fun onIngredientClicked(entity: IngredientUiEntity, position: Int) {

    }
}