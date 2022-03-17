package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.IngredientUiEntity

class IngredientHolder constructor(
    itemView: View,
    private val itemObserver: ItemObserver? = null
) : RecyclerView.ViewHolder(itemView) {


    fun bind(ingredientUiEntity: IngredientUiEntity) {

    }

    interface ItemObserver {
        fun onItemClicked(item: IngredientUiEntity)
    }
}