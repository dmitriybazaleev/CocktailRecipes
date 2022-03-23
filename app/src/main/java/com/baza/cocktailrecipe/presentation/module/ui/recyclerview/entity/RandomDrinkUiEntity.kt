package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import android.os.Parcelable
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.HomeAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
data class RandomDrinkUiEntity(
    val idDrink: String?,
    val strDrink: String?,
    val strThumb: String?
) : Parcelable, HomeUiEntity {
    override fun getHomeState(): HomeAdapter.HomeViewType =
        HomeAdapter.HomeViewType.RANDOM_COCKTAIL_TYPE

}