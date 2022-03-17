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
        success: (JsonObject) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            success.invoke(api.randomCocktail())

        } catch (e: Exception) {
            error.invoke(e)
        }
    }
}