package com.baza.cocktailrecipe.presentation.module.ui.event

import android.view.View
import androidx.annotation.StringRes

sealed class ErrorEvent {

    data class DialogEvent(
        val title: String?,
        val message: String?,
        val positiveButtonText: String? = null,
        val positiveButtonAction: ((v: View) -> Unit)? = null,
        val negativeButtonText: String? = null,
        val negativeButtonAction: ((v: View) -> Unit)? = null
    ) : ErrorEvent()

    data class DialogEventRes(
        @StringRes val titleRes: Int,
        @StringRes val messageRes: Int,
        @StringRes val positiveButtonRes: Int? = null,
        val positiveButtonAction: ((v: View) -> Unit)? = null,
        @StringRes val negativeButtonRes: Int? = null,
        val negativeButtonAction: ((v: View) -> Unit)? = null
    ) : ErrorEvent()
}