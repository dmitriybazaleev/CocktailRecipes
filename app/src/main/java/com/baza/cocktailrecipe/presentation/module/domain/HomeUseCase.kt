package com.baza.cocktailrecipe.presentation.module.domain

import com.baza.cocktailrecipe.presentation.module.data.api.CocktailApi
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.google.gson.JsonObject
import java.lang.Exception
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val api: CocktailApi
) {

    /**
     * Данный метод возвращает список рандомных коктейлей
     */
    suspend fun getRandomCocktail() = api.randomCocktail()

    /**
     * Возвращает список коктейлей по первой букве
     */
    suspend fun getCocktailsByLetter(
        digit: String
    ) = api.searchByFirstLetter(digit)

    /**
     * Данный запрос уходит на получение подробных данных о коктейле
     */
    suspend fun getCocktailById(
        cocktailId: String
    ) = api.lookupFullCocktailDetails(cocktailId)
}