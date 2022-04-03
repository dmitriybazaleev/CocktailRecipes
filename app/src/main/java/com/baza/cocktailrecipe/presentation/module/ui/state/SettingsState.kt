package com.baza.cocktailrecipe.presentation.module.ui.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingsState(
    var language: String? = null
) : Parcelable
