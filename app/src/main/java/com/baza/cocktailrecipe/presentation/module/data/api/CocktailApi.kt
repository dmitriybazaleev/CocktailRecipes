package com.baza.cocktailrecipe.presentation.module.data.api

import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception

interface CocktailApi {

    // Search cocktail by name
    @GET(SEARCH_METHOD)
    suspend fun searchByName(@Query("s") name: String): JsonObject

    // List all cocktails by first letter
    @GET(SEARCH_METHOD)
    suspend fun searchByFirstLetter(@Query("f") letter: String): JsonObject

    // Search ingredient by name
    @GET(SEARCH_METHOD)
    suspend fun searchByIngredient(@Query("i") ingredient: String): JsonObject

    // Lookup full cocktail details by id
    @GET(LOOK_UP_METHOD)
    suspend fun lookupFullCocktailDetails(@Query("i") details: Int)

    // Lookup ingredient by ID
    @GET(LOOK_UP_METHOD)
    suspend fun lookUpIngredientId(@Query("iid") id: Int)

    // Lookup a random cocktail
    @GET(RANDOM_COCKTAIL_METHOD)
    suspend fun randomCocktail(): JsonObject

    // Search by ingredient
    @GET(FILTER_METHOD)
    suspend fun filterByIngredient(@Query("i") ingredient: String)
}