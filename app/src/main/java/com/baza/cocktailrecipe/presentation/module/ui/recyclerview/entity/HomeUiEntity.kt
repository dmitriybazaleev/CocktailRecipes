package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.HomeAdapter

interface HomeUiEntity {
    fun getHomeState(): HomeAdapter.HomeViewType
}