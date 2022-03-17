package com.baza.cocktailrecipe.presentation.module.ui.state

import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity

data class SearchByIngredientState(
    var searchResult: List<DrinkEntity> = listOf(),
    var isShowProgress: Boolean = false,
    var isShowPlaceholder: Boolean = false
)