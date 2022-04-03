package com.baza.cocktailrecipe.presentation.module.ui.event

sealed class SelectLanguageEvent {

    class ChangeListEvent(
        val newSelectedPosition: Int,
        val previousPosition: Int
    ) : SelectLanguageEvent()
}