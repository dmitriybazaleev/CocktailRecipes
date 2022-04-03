package com.baza.cocktailrecipe.presentation.module.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LanguageEntity(
    val code: String,
    val name: String,
    val nativeName: String
) : Parcelable