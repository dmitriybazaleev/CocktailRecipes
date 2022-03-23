package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.ui.event.LauncherEvent
import com.baza.cocktailrecipe.presentation.module.ui.fragments.DEFAULT_SPLASH_DELAY
import com.baza.cocktailrecipe.presentation.module.ui.state.LauncherState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LauncherViewModel : ViewModel() {

    private val _mLauncherEvent = MutableSharedFlow<LauncherEvent>()

    private val _mLauncherState = MutableLiveData<LauncherState>()
    private val mState = LauncherState()

    init {
        delaySplash()
    }

    private fun delaySplash() {
        mState.isShowProgress = true
        updateState()
        viewModelScope.launch {
            delay(DEFAULT_SPLASH_DELAY)

            emitEvent(
                LauncherEvent.NavEvent(
                    R.id.action_launcherFragment_to_home_navigation
                )
            )

        }
    }

    private suspend fun emitEvent(event: LauncherEvent) = _mLauncherEvent.emit(event)

    private fun updateState() {
        _mLauncherState.value = mState
    }

    val launcherState: LiveData<LauncherState>
        get() = _mLauncherState

    val launcherEvent: SharedFlow<LauncherEvent>
        get() = _mLauncherEvent.asSharedFlow()
}