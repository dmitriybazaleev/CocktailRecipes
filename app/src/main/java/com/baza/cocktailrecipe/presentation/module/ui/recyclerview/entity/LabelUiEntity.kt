package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import android.os.Parcelable
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchByNameAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
data class LabelUiEntity(
    val searchLabel: String?
) : Parcelable, SearchNameUiEntity {
    override fun getViewType(): SearchByNameAdapter.SearchViewType =
        SearchByNameAdapter.SearchViewType.SEARCH_TEXT_TYPE

}