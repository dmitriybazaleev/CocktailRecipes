package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import android.os.Parcelable
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.HomeAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrinkByLetterUiEntity(
    val strDrink: String?,
    val strCategory: String?,
    val strAlcoholic: String? = null,
    val strGlass: String?,
    val strInstruction: String?,
    val strDrinkThumb: String?,
) : Parcelable, HomeUiEntity {
    override fun getHomeState(): HomeAdapter.HomeViewType =
        HomeAdapter.HomeViewType.COCKTAIL_BY_LETTER_TYPE
}
