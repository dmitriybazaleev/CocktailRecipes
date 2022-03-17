package com.baza.cocktailrecipe.presentation.module.ui.event

sealed class HomeEvent {

    data class NetworkError constructor(
        val title: String,
        val message: String
    ) : HomeEvent()
}
