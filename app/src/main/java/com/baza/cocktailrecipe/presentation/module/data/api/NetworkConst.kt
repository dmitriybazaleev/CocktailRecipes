package com.baza.cocktailrecipe.presentation.module.data.api

/**
 * Тут будут содержаться все данные о Api:
 * Базовая ссылка, поля json, все возможные методы
 * Благодарность api: https://www.thecocktaildb.com/api.php
 */

const val CONNECTION_TIME_OUT = 12000

// Базовая ссылка
const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"


// Search cocktail
const val SEARCH_METHOD = "search.php?"

// Lookup full cocktail details by id
const val LOOK_UP_METHOD = "lookup.php?"

// Lookup a random cocktail
const val RANDOM_COCKTAIL_METHOD = "random.php"

// Filter by
const val FILTER_METHOD = "filter.php?"

const val DRINKS = "drinks"
const val INGREDIENTS = "ingredients"
const val ID_DRINK = "idDrink"
const val STR_DRINK = "strDrink"
const val STR_CATEGORY = "strCategory"
const val STR_ALCOHOLIC = "strAlcoholic"
const val STR_GLASS = "strGlass"
const val STR_INSTRUCTION = "strInstructions"
const val STR_DRINK_THUMB = "strDrinkThumb"
const val STR_VIDEO = "strVideo"

const val ID_INGREDIENT = "idIngredient"
const val STR_INGREDIENT = "strIngredient"
const val STR_DESCRIPTION = "strDescription"
const val STR_TYPE = "strType"
const val STR_ALCOHOL = "strAlcohol"
const val STR_ABV = "strABV"


