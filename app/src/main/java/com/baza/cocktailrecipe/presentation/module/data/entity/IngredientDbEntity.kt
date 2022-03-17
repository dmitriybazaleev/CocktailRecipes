package com.baza.cocktailrecipe.presentation.module.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baza.cocktailrecipe.presentation.module.data.api.*
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Ingredients")
@Parcelize
data class IngredientDbEntity(
    @PrimaryKey(autoGenerate = true)
    val idIngredient: Int,
    @ColumnInfo(name = "strIngredient")
    val strIngredient: String?,
    @ColumnInfo(name = "strDescription")
    val strDescription: String?,
    @ColumnInfo(name = "strType")
    val strType: String?,
    @ColumnInfo(name = "strAlcohol")
    val strAlcohol: String?,
) : Parcelable