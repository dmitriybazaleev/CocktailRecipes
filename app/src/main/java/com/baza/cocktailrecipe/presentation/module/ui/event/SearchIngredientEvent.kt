package com.baza.cocktailrecipe.presentation.module.ui.event

import android.view.View
import androidx.annotation.StringRes
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity

sealed class SearchIngredientEvent {

    data class ShowCocktailDetailsEvent(
        val ingredientEntity: IngredientEntity
    ) : SearchIngredientEvent()
}