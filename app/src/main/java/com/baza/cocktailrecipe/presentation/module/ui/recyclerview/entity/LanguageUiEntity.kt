package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LanguageUiEntity(
    val name: String,
    val nativeName: String,
    val code: String,
    var isLanguageSelected: Boolean
) : Parcelable