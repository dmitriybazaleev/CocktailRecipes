package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import android.os.Parcelable
import androidx.annotation.StringRes
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchByNameAdapter
import kotlinx.parcelize.Parcelize

@Parcelize
data class LabelUiEntity(
    @StringRes val searchLabelRes: Int
) : Parcelable, SearchNameUiEntity {
    override fun getViewType(): SearchByNameAdapter.SearchViewType =
        SearchByNameAdapter.SearchViewType.SEARCH_TEXT_TYPE

}