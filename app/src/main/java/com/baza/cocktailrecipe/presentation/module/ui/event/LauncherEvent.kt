package com.baza.cocktailrecipe.presentation.module.ui.event

import androidx.annotation.IdRes

sealed class LauncherEvent {

    class NavEvent(@IdRes val destinationId: Int) : LauncherEvent()
}
