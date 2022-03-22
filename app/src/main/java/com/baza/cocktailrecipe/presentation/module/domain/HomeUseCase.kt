package com.baza.cocktailrecipe.presentation.module.domain

import com.baza.cocktailrecipe.presentation.module.data.api.CocktailApi
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.google.gson.JsonObject
import java.lang.Exception
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val api: CocktailApi
) {

    suspend fun getRandomCocktail(
        success: suspend (JsonObject) -> Unit,
        onError: suspend (e: Exception) -> Unit
    ) {
        try {
            success.invoke(api.randomCocktail())

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }
}