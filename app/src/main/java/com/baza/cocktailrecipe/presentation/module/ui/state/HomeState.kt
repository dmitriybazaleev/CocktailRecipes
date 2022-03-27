package com.baza.cocktailrecipe.presentation.module.ui.state

import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.RecommendationUiEntity

data class HomeState(
    var recommendationsList: List<RecommendationUiEntity> = mutableListOf(),
    var randomList: List<DrinkEntity> = mutableListOf(),
    var randomDrink: DrinkEntity? = null,
    var isShowProgress: Boolean = false,
    var isRefreshing: Boolean = false,
    var isShowPlaceholder: Boolean = false
)
