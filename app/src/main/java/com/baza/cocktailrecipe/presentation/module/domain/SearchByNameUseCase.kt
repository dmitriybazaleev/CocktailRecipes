package com.baza.cocktailrecipe.presentation.module.domain

import com.baza.cocktailrecipe.presentation.module.data.api.CocktailApi
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.data.room.DrinksDao
import com.google.gson.JsonObject
import javax.inject.Inject

class SearchByNameUseCase @Inject constructor(
    private val mApi: CocktailApi,
    private val mDao: DrinksDao
) {

    suspend fun onSearchCocktail(
        name: String,
        onSuccess: suspend (response: JsonObject) -> Unit,
        onError: suspend (e: Exception) -> Unit
    ) {
        try {
            onSuccess.invoke(mApi.searchByName(name))

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }

    suspend fun onInsertDrink(
        entity: DrinkEntity,
        onSuccess: suspend () -> Unit,
        onError: suspend (e: java.lang.Exception) -> Unit
    ) {
        try {
            mDao.insertDrink(entity)

            onSuccess.invoke()

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }

    suspend fun getSavedDrinks(
        onSuccess: suspend (response: List<DrinkEntity>) -> Unit,
        onError: suspend (e: Exception) -> Unit
    ) {
        try {
            onSuccess.invoke(mDao.getAllDrinks())

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }

    suspend fun onRemoveDrink(
        drinkId: Int,
        onSuccess: suspend () -> Unit,
        onError: suspend (e: Exception) -> Unit
    ) {
        try {
            mDao.removeDrink(drinkId)

            onSuccess.invoke()

        } catch (e: java.lang.Exception) {
            onError.invoke(e)
        }
    }
}