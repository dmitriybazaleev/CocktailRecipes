package com.baza.cocktailrecipe.presentation.module.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity

@Database(
    entities = [DrinkEntity::class, IngredientEntity::class],
    version = 5,
    exportSchema = false
)
abstract class RoomCreator : RoomDatabase() {

    abstract fun getDrinkDao(): DrinksDao
    abstract fun getIngredientsDao(): IngredientDao
}