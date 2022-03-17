package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.baza.cocktailrecipe.presentation.base.App
import com.baza.cocktailrecipe.presentation.module.domain.SavedUseCase
import javax.inject.Inject

class SavedViewModel : ViewModel() {

    @Inject
    lateinit var savedUseCase: SavedUseCase

    init {
        App.appComponent?.inject(this)
    }
}