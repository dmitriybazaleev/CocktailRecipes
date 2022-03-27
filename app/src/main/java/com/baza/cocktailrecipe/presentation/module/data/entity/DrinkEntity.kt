package com.baza.cocktailrecipe.presentation.module.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.baza.cocktailrecipe.presentation.module.data.api.*
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Drinks")
data class DrinkEntity constructor(

    @PrimaryKey(autoGenerate = true)
    var idRoomDrink: Int,

    @ColumnInfo(name = "strDrink")
    @SerializedName(STR_DRINK)
    var strDrink: String?,

    @SerializedName(STR_CATEGORY)
    @ColumnInfo(name = "strCategory")
    var strCategory: String?,

    @SerializedName(STR_ALCOHOLIC)
    @ColumnInfo(name = "strAlcoholic")
    var strAlcoholic: String?,

    @ColumnInfo(name = "strGlass")
    @SerializedName(STR_GLASS)
    var strGlass: String?,

    @ColumnInfo(name = "strInstruction")
    @SerializedName(STR_INSTRUCTION)
    var strInstruction: String?,

    @ColumnInfo(name = "strDrinkThumb")
    @SerializedName(STR_DRINK_THUMB)
    var strDrinkThumb: String?,

    @ColumnInfo(name = "strVideo")
    @SerializedName(STR_VIDEO)
    var strVideo: String?
) : Parcelable