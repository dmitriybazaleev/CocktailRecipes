package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.ui.loadCircleImage
import com.baza.cocktailrecipe.presentation.module.ui.loadGlide
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkUiEntitySearch

class SearchCocktailHolder constructor(
    itemView: View,
    private val itemObserver: ItemObserver? = null
) : RecyclerView.ViewHolder(itemView) {

    private val mCocktailImage: ImageView = itemView.findViewById(R.id.iv_cocktail_avatar)
    private val mCocktailName: TextView = itemView.findViewById(R.id.txv_cocktail_name)

    fun bind(entity: DrinkUiEntitySearch) {
        mCocktailName.text = entity.strDrink
        mCocktailImage.loadCircleImage(entity.strDrinkThumb, R.drawable.icn_drink_placeholder)

        itemView.setOnClickListener {
            itemObserver?.onItemClicked(entity, adapterPosition)
        }
    }

    interface ItemObserver {
        fun onItemClicked(item: DrinkUiEntitySearch, itemPosition: Int)
    }
}