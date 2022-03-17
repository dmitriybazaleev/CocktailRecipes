package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.ui.doDelay
import com.baza.cocktailrecipe.presentation.module.ui.event.LauncherEvent
import com.baza.cocktailrecipe.presentation.module.ui.fragments.DEFAULT_SPLASH_DELAY
import com.baza.cocktailrecipe.presentation.module.ui.state.LauncherState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LauncherViewModel : ViewModel() {

    private val _mLauncherEvent = MutableSharedFlow<LauncherEvent>()

    private val _mLauncherState = MutableLiveData<LauncherState>()
    private val mState = LauncherState()

    val launcherEvent
        get() = _mLauncherEvent.asSharedFlow()

    init {
        checkNavigation()
    }

    private fun checkNavigation() {
        // TODO: 15.03.2022 Test
        mState.isShowProgress = true
        updateState()
        viewModelScope.launch {
            delay(DEFAULT_SPLASH_DELAY)

            _mLauncherEvent.emit(
                LauncherEvent.NavEvent(
                    R.id.action_launcherFragment_to_home_navigation
                )
            )

        }
    }

    private fun updateState() {
        _mLauncherState.value = mState
    }

    val launcherState: LiveData<LauncherState>
        get() = _mLauncherState
}