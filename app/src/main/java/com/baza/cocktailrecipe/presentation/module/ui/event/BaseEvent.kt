package com.baza.cocktailrecipe.presentation.module.ui.event

import android.view.View
import androidx.annotation.StringRes

sealed class BaseEvent {

    class ActionDialogEvent(
        val title: String,
        val message: String,
        val positiveButtonText: String? = null,
        val positionButtonAction: ((v: View) -> Unit)? = null,
        val negativeButtonText: String? = null,
        val negativeButtonAction: ((v: View) -> Unit)? = null
    ) : BaseEvent()

    class ActionDialogEventRes(
        @StringRes val title: Int,
        @StringRes val message: Int,
        @StringRes val positiveButtonText: Int? = null,
        val positionButtonAction: ((v: View) -> Unit)? = null,
        @StringRes val negativeButtonText: Int? = null,
        val negativeButtonAction: ((v: View) -> Unit)? = null
    ) : BaseEvent()
}
