package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendationUiEntity(
    val cocktailName: String?,
    val isSelected: Boolean
) : Parcelable
