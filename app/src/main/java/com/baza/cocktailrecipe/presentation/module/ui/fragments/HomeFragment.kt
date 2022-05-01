package com.baza.cocktailrecipe.presentation.module.ui.fragments

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
import com.baza.cocktailrecipe.presentation.module.ui.blur.BlurHelper
import com.baza.cocktailrecipe.presentation.module.ui.dialog.FullCocktailInfoDialog
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.RandomCocktailAdapter
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.RecommendationAdapter
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.RecommendationUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.CocktailHolder
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.RecommendationHolder
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

const val TAG = "homeTag"

class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    SwipeRefreshLayout.OnRefreshListener, RecommendationHolder.RecommendationItemObserver,
    CocktailHolder.CocktailItemObserver {

    private val viewModel by viewModels<HomeViewModel>()

    private var eventJob: Job? = null
    private val recommendationAdapter = RecommendationAdapter(this)
    private val randomCocktailAdapter = RandomCocktailAdapter(this)

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
            recommendationAdapter.updateRecommendations(state.recommendationsList)
            randomCocktailAdapter.updateList(state.randomList)
            state?.randomDrink?.let { random ->
                updateRandomDrinkData(random)
            }
        }
    }

    private fun updateRandomDrinkData(entity: DrinkEntity) {
        BlurHelper(
            requireContext(),
            viewLifecycleOwner
        ).blurByUrl(entity.strDrinkThumb) { blurBitmap ->
            binding?.ivRandomCocktailBlur?.setImageBitmap(blurBitmap)
        }
        binding?.txvRandomCocktailName?.text = entity.strDrink
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

    override fun onRefresh() {
    }

    override fun onRecommendationItemClicked(entity: RecommendationUiEntity) {
        Log.d(TAG, "recommendation item: $entity")
    }

    override fun onItemClicked(entity: DrinkEntity) {
        FullCocktailInfoDialog.create(entity)
            .show(childFragmentManager, FullCocktailInfoDialog.TAG)
    }
}