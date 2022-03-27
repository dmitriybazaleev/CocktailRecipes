package com.baza.cocktailrecipe.presentation.module.ui.state

import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.SearchNameUiEntity

data class SearchByNameState(
    var searchResult: MutableList<SearchNameUiEntity> = mutableListOf(),
    var isShowProgress: Boolean = false,
    var isShowPlaceholder: Boolean = false
)