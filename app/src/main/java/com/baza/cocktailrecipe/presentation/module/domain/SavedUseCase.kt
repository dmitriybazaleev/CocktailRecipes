package com.baza.cocktailrecipe.presentation.module.domain

import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkDatabaseEntity
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientDbEntity
import com.baza.cocktailrecipe.presentation.module.data.room.DrinksDao
import com.baza.cocktailrecipe.presentation.module.data.room.IngredientDao
import java.lang.Exception
import javax.inject.Inject

class SavedUseCase @Inject constructor(
    private val drinksDao: DrinksDao,
    private val ingredientsDao: IngredientDao
) {

    suspend fun getDrinks(
        onSuccess: (List<DrinkDatabaseEntity>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            onSuccess.invoke(drinksDao.getAllDrinks())

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }

    suspend fun getIngredients(
        onSuccess: (List<IngredientDbEntity>) -> Unit,
        onError: (e: Exception) -> Unit
    ) {
        try {
            onSuccess.invoke(ingredientsDao.getAllIngredients())
        } catch (e: Exception) {
            onError.invoke(e)
        }
    }
}