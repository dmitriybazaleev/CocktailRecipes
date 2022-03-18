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
    ): JsonObject = mApi.searchByName(name)

    suspend fun onInsertDrink(entity: DrinkEntity) =
        mDao.insertDrink(entity)

    suspend fun getSavedDrinks(): List<DrinkEntity> =
        mDao.getAllDrinks()

    suspend fun onRemoveDrink(
        drinkId: Int
    ) = mDao.removeDrink(drinkId)
}