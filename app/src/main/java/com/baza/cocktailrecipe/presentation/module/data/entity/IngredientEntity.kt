package com.baza.cocktailrecipe.presentation.module.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baza.cocktailrecipe.presentation.module.data.api.*
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Ingredients")
data class IngredientEntity(

    @SerializedName(ID_INGREDIENT)
    @PrimaryKey
    val idIngredientStr: String,

    @SerializedName(STR_INGREDIENT)
    @ColumnInfo(name = "strIngredient")
    val strIngredient: String?,

    @ColumnInfo(name = "strDescription")
    @SerializedName(STR_DESCRIPTION)
    val strDescription: String?,

    @ColumnInfo(name = "strType")
    @SerializedName(STR_TYPE)
    val strType: String?,

    @ColumnInfo(name = "strAlcohol")
    @SerializedName(STR_ALCOHOL)
    val strAlcohol: String?,

    @ColumnInfo(name = "strAbv")
    @SerializedName(STR_ABV)
    val strAbv: String?
) : Parcelable
