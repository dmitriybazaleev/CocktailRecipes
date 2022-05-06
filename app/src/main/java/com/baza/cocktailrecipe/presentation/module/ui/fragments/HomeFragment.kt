package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.baza.cocktailrecipe.databinding.FragmentHomeBinding
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.blur.IBlurHelper
import com.baza.cocktailrecipe.presentation.module.ui.dialog.FullCocktailInfoDialog
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.RandomCocktailAdapter
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.RecommendationAdapter
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.RecommendationUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.CocktailHolder
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.RecommendationHolder
import com.baza.cocktailrecipe.presentation.module.ui.state.HomeState
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

const val TAG = "homeTag"

class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    SwipeRefreshLayout.OnRefreshListener, RecommendationHolder.RecommendationItemObserver,
    CocktailHolder.CocktailItemObserver {

    @Inject
    lateinit var blurUtil: IBlurHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        act?.actSubcomponent?.inject(this)
    }

    private val viewModel by viewModels<HomeViewModel>()

    private var eventJob: Job? = null
    private var recommendationAdapter = RecommendationAdapter(this)
    private var randomCocktailAdapter = RandomCocktailAdapter(this)

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStateObserver()
        observeEvent()
        addListeners()
        initRecycler()
    }

    private fun initRecycler() {
        binding?.rvRecommendation?.adapter = recommendationAdapter
        binding?.rvRandom?.adapter = randomCocktailAdapter
    }

    private fun addListeners() {
        binding?.slHome?.setOnRefreshListener(this)
    }

    private fun addStateObserver() {
        viewModel.homeLiveData.observe(viewLifecycleOwner) { state ->
            updateProgressState(state.isShowProgress)
            updateSwipeState(state.isRefreshing)
            updateData(state)
        }
    }

    private fun updateData(state: HomeState) {
        recommendationAdapter.updateRecommendations(state.recommendationsList)
        randomCocktailAdapter.updateList(state.randomList)
        binding?.txvRandomCocktailName?.text = state.randomDrink?.strDrink

        lifecycleScope.launch(Dispatchers.Default) {
            blurUtil.blurByUrl(state.randomDrink?.strDrinkThumb) { blurBitmap ->
                binding?.ivRandomCocktailBlur?.setImageBitmap(blurBitmap)
            }
        }
    }

    private fun updateSwipeState(isSwiped: Boolean) {
        binding?.slHome?.isRefreshing = isSwiped
    }

    private fun observeEvent() {
        eventJob = viewModel.homeEvent
            .onEach { event ->
            }
            .launchIn(lifecycleScope)
    }

    private fun updateProgressState(isShow: Boolean) {
        binding?.pbHome?.isVisible = isShow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        eventJob?.cancel()
    }

    override fun onRefresh() {
        viewModel.getCocktails(true)
    }

    override fun onRecommendationItemClicked(entity: RecommendationUiEntity) {
        Log.d(TAG, "recommendation item: $entity")
    }

    override fun onItemClicked(entity: DrinkEntity) {
        FullCocktailInfoDialog.create(entity)
            .show(childFragmentManager, FullCocktailInfoDialog.TAG)
    }
}