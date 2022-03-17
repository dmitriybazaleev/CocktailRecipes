package com.baza.cocktailrecipe.presentation.module.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkDatabaseEntity

@Dao
abstract class DrinksDao {

    @Query("SELECT * FROM Drinks")
    abstract suspend fun getAllDrinks(): List<DrinkDatabaseEntity>

    @Query("SELECT COUNT(idDrink) FROM Drinks")
    abstract suspend fun getRowCount(): Int

    @Query("SELECT * FROM Drinks WHERE strDrink =:drink")
    abstract suspend fun getDrinkByName(drink: String): DrinkDatabaseEntity

    @Query("DELETE FROM DRINKS")
    abstract suspend fun clearDrinks()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertDrinks(newList: List<DrinkDatabaseEntity>)
}