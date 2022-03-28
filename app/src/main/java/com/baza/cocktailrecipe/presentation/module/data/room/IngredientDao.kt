package com.baza.cocktailrecipe.presentation.module.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity

@Dao
abstract class IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertIngredients(newData: List<IngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertIngredient(newData: IngredientEntity)

    @Query("SELECT * FROM Ingredients")
    abstract suspend fun getAllIngredients(): List<IngredientEntity>

    @Query("SELECT * FROM Ingredients WHERE strIngredient =:ingredient")
    abstract suspend fun getIngredientByName(ingredient: String): IngredientEntity

    @Query("DELETE FROM INGREDIENTS")
    abstract suspend fun clear()

    @Query("DELETE FROM Ingredients WHERE idIngredientStr =:id")
    abstract suspend fun onRemoveIngredientById(id: String)
}