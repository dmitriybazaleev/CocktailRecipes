package com.baza.cocktailrecipe.presentation.module.ui.state

import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.SearchUiEntity

data class SearchByNameState(
    var searchResult: List<SearchUiEntity> = mutableListOf(),
    var isShowProgress: Boolean = false,
    var isShowPlaceholder: Boolean = false
)