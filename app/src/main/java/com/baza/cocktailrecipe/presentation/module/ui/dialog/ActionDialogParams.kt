package com.baza.cocktailrecipe.presentation.module.ui.dialog

import android.os.Parcelable
import android.view.View
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActionDialogParams(
    val actionDialogTitle: String,
    val actionDialogMessage: String,
    val icon: Int?,
    val positiveButtonText: String? = null,
    val positiveButtonAction: ((v: View) -> Unit)? = null,
    val negativeButtonText: String? = null,
    val negativeButtonAction: ((v: View) -> Unit)? = null
) : Parcelable