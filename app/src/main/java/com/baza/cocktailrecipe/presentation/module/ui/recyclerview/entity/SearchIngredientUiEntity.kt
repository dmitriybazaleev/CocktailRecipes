package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchIngredientAdapter

interface SearchIngredientUiEntity {

    fun getIngredientViewType(): SearchIngredientAdapter.IngredientViewType
}