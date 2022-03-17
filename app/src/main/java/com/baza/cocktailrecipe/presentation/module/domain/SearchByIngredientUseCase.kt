package com.baza.cocktailrecipe.presentation.module.domain

import com.baza.cocktailrecipe.presentation.module.data.api.CocktailApi
import com.google.gson.JsonObject
import javax.inject.Inject

class SearchByIngredientUseCase @Inject constructor(
    private val mApi: CocktailApi
) {

    suspend fun onSearchIngredient(
        query: String,
        onSuccess: (JsonObject) -> Unit,
        onError: (e: Exception) -> Unit
    ) {
        try {
            onSuccess.invoke(mApi.searchByIngredient(query))

        } catch (e: Exception) {
            onError(e)
        }
    }
}