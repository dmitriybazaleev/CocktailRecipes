package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.baza.cocktailrecipe.databinding.FragmentSearchByNameBinding
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.dialog.FullCocktailInfoDialog
import com.baza.cocktailrecipe.presentation.module.ui.event.SearchEvent
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchByNameAdapter
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toDrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkUiEntitySearch
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.SearchCocktailHolder
import com.baza.cocktailrecipe.presentation.module.ui.textChanges
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SearchByNameViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

class SearchByNameFragment : BaseFragment<FragmentSearchByNameBinding>(),
    SearchCocktailHolder.ItemObserver {

    private val viewModel by viewModels<SearchByNameViewModel>()

    private var mSearchAdapter: SearchByNameAdapter? = null
    private var mJob: Job? = null

    companion object {

        @JvmStatic
        fun create() = SearchByNameFragment()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchByNameBinding
        get() = FragmentSearchByNameBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWithBaseViewModel(viewModel)
        addStateObserver()
        setUpRecycler()
        initListeners()
        addEventObserver()
    }

    private fun addEventObserver() {
        viewModel.searchEvent
            .onEach { event ->
                when (event) {
                    is SearchEvent.ShowCocktailEvent -> {
                        showCocktailInfoDialog(entity = event.selectedEntity)
                    }
                    is SearchEvent.RestorePositionEvent -> {
                        // TODO: 20.03.2022 Пока ничего не делаем!
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun setUpRecycler() {
        mSearchAdapter = SearchByNameAdapter(this)
        binding?.rvSearchByName?.adapter = mSearchAdapter

        mSearchAdapter?.attachItemTouch(binding?.rvSearchByName) { item, holder ->
            viewModel.onRemoveItem(item, holder.adapterPosition)
        }
    }

    private fun addStateObserver() {
        viewModel.searchByNameLiveData.observe(viewLifecycleOwner) { state ->
            updateProgress(state.isShowProgress)
            mSearchAdapter?.updateList(state.searchResult)
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
                        viewModel.getSavedCocktails()
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

    private fun showCocktailInfoDialog(
        entity: DrinkEntity,
    ) {
        val dialog = FullCocktailInfoDialog.create(entity)
        dialog.show(childFragmentManager, FullCocktailInfoDialog.TAG)
    }

    override fun onStop() {
        super.onStop()
        mJob?.cancel()
    }

    override fun onDestroyView() {
        disposeAdapter()
        super.onDestroyView()
    }

    private fun disposeAdapter() {
        mSearchAdapter = null
        binding?.rvSearchByName?.adapter = null
    }

    override fun onItemClicked(item: DrinkUiEntitySearch, itemPosition: Int) {
        if (item.isSavedList) {
            /**
             * Текущие данные, которые показывает список из Room
             */
            showCocktailInfoDialog(item.toDrinkEntity())

        } else {
            /**
             * Текущее данные, которые показывает список из сервера
             * Кэшируем данные в базу
             */
            viewModel.onInsertClickedCocktails(item)
        }
    }
}