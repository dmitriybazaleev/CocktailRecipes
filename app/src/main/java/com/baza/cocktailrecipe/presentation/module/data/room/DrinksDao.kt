package com.baza.cocktailrecipe.presentation.module.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity

@Dao
abstract class DrinksDao {

    @Query("SELECT * FROM Drinks")
    abstract suspend fun getAllDrinks(): List<DrinkEntity>

    @Query("SELECT COUNT(idRoomDrink) FROM Drinks")
    abstract suspend fun getRowCount(): Int

    @Query("SELECT * FROM Drinks WHERE strDrink =:drink")
    abstract suspend fun getDrinkByName(drink: String): DrinkEntity

    @Query("DELETE FROM DRINKS")
    abstract suspend fun clearDrinks()

    @Query("DELETE FROM Drinks WHERE idRoomDrink =:drinkId")
    abstract suspend fun removeDrink(drinkId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertDrinks(newList: List<DrinkEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertDrink(drink: DrinkEntity)
}