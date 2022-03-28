package com.baza.cocktailrecipe.presentation.module.domain

import com.baza.cocktailrecipe.presentation.module.data.api.CocktailApi
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity
import com.baza.cocktailrecipe.presentation.module.data.room.IngredientDao
import com.google.gson.JsonObject
import javax.inject.Inject

class SearchByIngredientUseCase @Inject constructor(
    private val mApi: CocktailApi,
    private val mDao: IngredientDao
) {

    suspend fun onSearchIngredient(
        query: String,
        onSuccess: suspend (response: JsonObject) -> Unit,
        onError: suspend (e: Exception) -> Unit
    ) {
        try {
            onSuccess.invoke(mApi.searchByIngredient(query))

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }

    suspend fun getSearchHistory(
        onSuccess: suspend (ingredients: List<IngredientEntity>) -> Unit,
        onError: suspend (e: Exception) -> Unit
    ) {
        try {
            onSuccess.invoke(mDao.getAllIngredients())

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }

    suspend fun onInsertIngredient(
        item: IngredientEntity,
        onSuccess: suspend () -> Unit,
        onError: suspend (e: Exception) -> Unit
    ) {
        try {
            mDao.insertIngredient(item)

            onSuccess.invoke()

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }

    suspend fun onRemoveIngredient(
        ingredientId: String,
        onSuccess: suspend () -> Unit,
        onError: suspend (e: Exception) -> Unit
    ) {
        try {
            mDao.onRemoveIngredientById(ingredientId)

            onSuccess.invoke()

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }
}