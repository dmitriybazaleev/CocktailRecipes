package com.baza.cocktailrecipe.presentation.module.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkDatabaseEntity
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientDbEntity

@Database(
    entities = [DrinkDatabaseEntity::class, IngredientDbEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RoomCreator : RoomDatabase() {

    abstract fun getDrinkDao(): DrinksDao
    abstract fun getIngredientsDao(): IngredientDao
}