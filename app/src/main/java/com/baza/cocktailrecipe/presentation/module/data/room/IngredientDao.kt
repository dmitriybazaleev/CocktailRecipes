package com.baza.cocktailrecipe.presentation.module.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientDbEntity

@Dao
abstract class IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertIngredient(newData: List<IngredientDbEntity>)

    @Query("SELECT * FROM Ingredients")
    abstract suspend fun getAllIngredients(): List<IngredientDbEntity>

    @Query("SELECT * FROM Ingredients WHERE strIngredient =:ingredient")
    abstract suspend fun getIngredientByName(ingredient: String): IngredientDbEntity

    @Query("DELETE FROM INGREDIENTS")
    abstract suspend fun clear()
}