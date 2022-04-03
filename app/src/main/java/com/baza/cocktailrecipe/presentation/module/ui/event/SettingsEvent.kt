package com.baza.cocktailrecipe.presentation.module.ui.event

sealed class SettingsEvent {

    class UpdateNightModeEvent(
        val isDarkMode: Boolean
    ) : SettingsEvent()
}