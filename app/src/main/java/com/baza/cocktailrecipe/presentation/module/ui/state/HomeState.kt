package com.baza.cocktailrecipe.presentation.module.ui.state

import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.HomeUiEntity

data class HomeState(
    val cocktailsList: MutableList<HomeUiEntity> = mutableListOf(),
    var isShowProgress: Boolean = false,
    var isRefreshing: Boolean = false
)
