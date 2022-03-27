package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import android.os.Parcelable
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchByNameAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrinkUiEntitySearch(
    val idDrink: Int?,
    val strDrink: String?,
    val strCategory: String?,
    val strAlcoholic: String?,
    val strGlass: String?,
    val strInstruction: String?,
    val strDrinkThumb: String?,
    val strVideo: String?,
    val isSavedList: Boolean
) : Parcelable, SearchNameUiEntity {
    override fun getViewType(): SearchByNameAdapter.SearchViewType =
        SearchByNameAdapter.SearchViewType.SEARCH_RESULT_TYPE
}