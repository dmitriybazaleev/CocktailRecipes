package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import android.os.Parcelable
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchIngredientAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientUiEntity(
    val strIngredient: String?,
    val strDescription: String?,
    val strType: String?,
    val strAlcohol: String?
) : Parcelable, SearchIngredientEntity {
    override fun getViewType(): SearchIngredientAdapter.SearchIngredientViewType =
        SearchIngredientAdapter.SearchIngredientViewType.SEARCH_ITEM
}
