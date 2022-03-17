package com.baza.cocktailrecipe.presentation.module.domain

import com.baza.cocktailrecipe.presentation.module.data.api.CocktailApi
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.google.gson.JsonObject
import java.lang.Exception
import javax.inject.Inject

class SearchByNameUseCase @Inject constructor(
    private val mApi: CocktailApi
) {

    suspend fun onSearchCocktail(
        name: String,
        onSuccess: (JsonObject) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            onSuccess.invoke(mApi.searchByName(name))

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }
}