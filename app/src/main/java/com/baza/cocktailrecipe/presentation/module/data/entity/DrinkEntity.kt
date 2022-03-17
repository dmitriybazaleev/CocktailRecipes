package com.baza.cocktailrecipe.presentation.module.data.entity

import android.os.Parcelable
import com.baza.cocktailrecipe.presentation.module.data.api.*
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrinkEntity(
    @SerializedName(ID_DRINK)
    val idDrink: String?,
    @SerializedName(STR_DRINK)
    val strDrink: String?,
    @SerializedName(STR_CATEGORY)
    val strCategory: String?,
    @SerializedName(STR_ALCOHOLIC)
    val strAlcoholic: String?,
    @SerializedName(STR_GLASS)
    val strGlass: String?,
    @SerializedName(STR_INSTRUCTION)
    val strInstruction: String?,
    @SerializedName(STR_DRINK_THUMB)
    val strDrinkThumb: String?,
    @SerializedName(STR_VIDEO)
    val strVideo: String?
) : Parcelable