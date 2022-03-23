package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkByLetterUiEntity

class CocktailByLetterHolder constructor(
    itemView: View,
    private val itemObserver: ItemObserver<DrinkByLetterUiEntity>? = null
) : RecyclerView.ViewHolder(itemView) {

    fun bind(itemEntity: DrinkByLetterUiEntity) {

    }
}