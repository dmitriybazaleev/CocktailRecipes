package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.presentation.module.data.PreferencesCache
import com.baza.cocktailrecipe.presentation.module.ui.event.SettingsEvent
import com.baza.cocktailrecipe.presentation.module.ui.state.SettingsState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val _settingsLiveData = MutableLiveData<SettingsState>()
    private val mSettingsState = SettingsState()

    private val _settingsEvent = MutableSharedFlow<SettingsEvent>()

    init {
        getLocalPreferences()
    }

    private fun getLocalPreferences() {
        mSettingsState.language = PreferencesCache.language
        updateUi()
    }

    private fun updateUi() {
        _settingsLiveData.value = mSettingsState
    }

    private suspend fun emitEvent(event: SettingsEvent) {
        _settingsEvent.emit(event)
    }


    val settingsLiveData: LiveData<SettingsState>
        get() = _settingsLiveData

    val settingsEvent: SharedFlow<SettingsEvent>
        get() = _settingsEvent.asSharedFlow()
}