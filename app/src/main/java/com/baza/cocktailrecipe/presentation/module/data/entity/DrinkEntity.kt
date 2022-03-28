package com.baza.cocktailrecipe.presentation.module.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baza.cocktailrecipe.presentation.module.data.api.*
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Drinks")
data class DrinkEntity constructor(

    @SerializedName(ID_DRINK)
    @PrimaryKey
    val drinkId: String,

    @ColumnInfo(name = "strDrink")
    @SerializedName(STR_DRINK)
    val strDrink: String?,

    @SerializedName(STR_CATEGORY)
    @ColumnInfo(name = "strCategory")
    val strCategory: String?,

    @SerializedName(STR_ALCOHOLIC)
    @ColumnInfo(name = "strAlcoholic")
    val strAlcoholic: String?,

    @ColumnInfo(name = "strGlass")
    @SerializedName(STR_GLASS)
    val strGlass: String?,

    @ColumnInfo(name = "strInstruction")
    @SerializedName(STR_INSTRUCTION)
    val strInstruction: String?,

    @ColumnInfo(name = "strDrinkThumb")
    @SerializedName(STR_DRINK_THUMB)
    val strDrinkThumb: String?,

    @ColumnInfo(name = "strVideo")
    @SerializedName(STR_VIDEO)
    val strVideo: String?
) : Parcelable