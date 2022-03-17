package com.baza.cocktailrecipe.presentation.module.data.entity

import android.os.Parcelable
import com.baza.cocktailrecipe.presentation.module.data.api.*
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngredientEntity(
    @SerializedName(ID_INGREDIENT)
    val idIngredient: String?,
    @SerializedName(STR_INGREDIENT)
    val strIngredient: String?,
    @SerializedName(STR_DESCRIPTION)
    val strDescription: String?,
    @SerializedName(STR_TYPE)
    val strType: String?,
    @SerializedName(STR_ALCOHOL)
    val strAlcohol: String?,
    @SerializedName(STR_ABV)
    val strAbv: String?
) : Parcelable
