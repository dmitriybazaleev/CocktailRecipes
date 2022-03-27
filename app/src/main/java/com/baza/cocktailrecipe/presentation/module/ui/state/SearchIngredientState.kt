package com.baza.cocktailrecipe.presentation.module.ui.state

import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.SearchIngredientUiEntity

data class SearchIngredientState(
    var searchResult: MutableList<SearchIngredientUiEntity> = mutableListOf(),
    var isShowProgress: Boolean = false,
    var isShowPlaceholder: Boolean = false,
)