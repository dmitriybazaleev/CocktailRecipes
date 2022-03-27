package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchByNameAdapter

interface SearchNameUiEntity {

    fun getViewType(): SearchByNameAdapter.SearchViewType
}