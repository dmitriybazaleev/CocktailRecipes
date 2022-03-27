package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import android.os.Parcelable
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchIngredientAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientUiEntity(
    val strIngredient: String?,
    val strDescription: String?,
    val strType: String?,
    val strAlcohol: String?,
    val isCurrentListSaved: Boolean
) : Parcelable, SearchIngredientUiEntity {
    override fun getIngredientViewType(): SearchIngredientAdapter.IngredientViewType =
        SearchIngredientAdapter.IngredientViewType.INGREDIENT_TYPE

}