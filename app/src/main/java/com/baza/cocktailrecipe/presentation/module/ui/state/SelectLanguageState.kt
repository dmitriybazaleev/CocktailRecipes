package com.baza.cocktailrecipe.presentation.module.ui.state

import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.LanguageUiEntity

data class SelectLanguageState(
    var languagesList: List<LanguageUiEntity> = listOf(),
    var isButtonEnabled: Boolean = false
)
