package com.baza.cocktailrecipe.presentation.module.ui.state

import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity

data class HomeState(
    var randomCocktail: List<DrinkEntity> = mutableListOf(),
    var isShowProgress: Boolean = false,
    var isRefreshing: Boolean = false
)
