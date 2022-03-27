package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.IngredientUiEntity

class IngredientHolder constructor(
    itemView: View,
    private val itemObserver: IngredientItemObserver? = null
) : RecyclerView.ViewHolder(itemView) {

    fun bind(itemEntity: IngredientUiEntity) {
        itemView.setOnClickListener {
            itemObserver?.onIngredientClicked(itemEntity, position = adapterPosition)
        }
    }


    interface IngredientItemObserver {
        fun onIngredientClicked(entity: IngredientUiEntity, position: Int)
    }
}