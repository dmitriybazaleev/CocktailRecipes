package com.baza.cocktailrecipe.presentation.module.ui.event

import android.view.View
import androidx.annotation.StringRes

sealed class SearchEvent {

    data class DialogEvent(
        @StringRes val titleRes: Int,
        @StringRes val messageRes: Int,
        @StringRes val positiveButtonTextRes: Int = 0,
        val positiveButtonAction: ((v: View) -> Unit)? = null,
        @StringRes val negativeButtonTextRes: Int = 0,
        val negativeButtonAction: ((v: View) -> Unit)? = null,
    ) : SearchEvent()
}
