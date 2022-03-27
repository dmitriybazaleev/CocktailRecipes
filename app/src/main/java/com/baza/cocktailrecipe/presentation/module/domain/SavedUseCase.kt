package com.baza.cocktailrecipe.presentation.module.domain

import com.baza.cocktailrecipe.presentation.module.data.room.DrinksDao
import com.baza.cocktailrecipe.presentation.module.data.room.IngredientDao
import javax.inject.Inject

class SavedUseCase @Inject constructor(
    private val drinksDao: DrinksDao,
    private val ingredientsDao: IngredientDao
) {

}