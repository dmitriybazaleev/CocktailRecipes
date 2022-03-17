package com.baza.cocktailrecipe.presentation.module.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Drinks")
@Parcelize
data class DrinkDatabaseEntity(
    @PrimaryKey(autoGenerate = true)
    val idDrink: Int,

    @ColumnInfo(name = "strDrink")
    val strDrink: String?,

    @ColumnInfo(name = "strCategory")
    val strCategory: String?,

    @ColumnInfo(name = "strInstruction")
    val strInstruction: String?,

    @ColumnInfo(name = "strDrinkThumb")
    val strDrinkThumb: String?
) : Parcelable