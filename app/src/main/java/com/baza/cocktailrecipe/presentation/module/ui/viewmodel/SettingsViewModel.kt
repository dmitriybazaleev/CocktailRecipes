package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baza.cocktailrecipe.presentation.module.data.PreferencesCache
import com.baza.cocktailrecipe.presentation.module.ui.state.SettingsState

class SettingsViewModel : ViewModel() {

    private val _settingsLiveData = MutableLiveData<SettingsState>()
    private val mSettingsState = SettingsState()

    init {
        getLocalPreferences()
    }

    private fun getLocalPreferences() {
        mSettingsState.isNightMode = PreferencesCache.isNightMode
        updateUi()
    }

    private fun updateUi() {
        _settingsLiveData.value = mSettingsState
    }

    fun updateNightMode(isNightMode: Boolean) {
        if (mSettingsState.isNightMode != isNightMode) {
            PreferencesCache.isNightMode = isNightMode
            mSettingsState.isNightMode = isNightMode
            updateUi()
        }
    }

    val settingsLiveData: LiveData<SettingsState>
        get() = _settingsLiveData
}