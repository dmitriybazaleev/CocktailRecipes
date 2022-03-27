package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.loadCircleImage
import com.baza.cocktailrecipe.presentation.module.ui.setTextOrHide

class CocktailHolder constructor(
    itemView: View,
    private val itemObserver: CocktailItemObserver? = null
) : RecyclerView.ViewHolder(itemView) {

    private val mCocktailDrinkName = itemView.findViewById<TextView>(R.id.txv_cocktail_name)
    private val mCocktailCategory = itemView.findViewById<TextView>(R.id.txv_cocktail_category)
    private val mCocktailDescription =
        itemView.findViewById<TextView>(R.id.txv_cocktail_description)
    private val mCocktailImage = itemView.findViewById<ImageView>(R.id.imv_cocktail)
    private val mCocktailVideoUrl = itemView.findViewById<TextView>(R.id.txv_cocktail_video)

    fun bind(itemEntity: DrinkEntity) {
        mCocktailVideoUrl.isVisible = itemEntity.strVideo != null
        mCocktailDrinkName.setTextOrHide(itemEntity.strDrink)
        mCocktailCategory.setTextOrHide(itemEntity.strCategory)
        mCocktailDescription.setTextOrHide(itemEntity.strInstruction)
        mCocktailImage.loadCircleImage(itemEntity.strDrinkThumb)

        itemView.setOnClickListener {
            itemObserver?.onItemClicked(entity = itemEntity)
        }

        mCocktailVideoUrl?.setOnClickListener {
            itemObserver?.onVideoUrlSelected(itemEntity.strVideo ?: "")
        }
    }

    interface CocktailItemObserver {
        fun onItemClicked(entity: DrinkEntity)
        fun onVideoUrlSelected(url: String)
    }
}