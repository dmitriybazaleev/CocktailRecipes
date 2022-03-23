package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.ui.blur.BlurHelper
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.RandomDrinkUiEntity

class RandomCocktailHolder constructor(
    itemView: View,
    private val itemObserver: ItemObserver<RandomDrinkUiEntity>? = null
) : RecyclerView.ViewHolder(itemView) {

    private val mDrinkImage = itemView.findViewById<ImageView>(R.id.iv_random_cocktail_blur)
    private val mDrinkText = itemView.findViewById<TextView>(R.id.txv_random_cocktail_name)

    fun bind(itemEntity: RandomDrinkUiEntity, lifecycleOwner: LifecycleOwner) {
        BlurHelper(
            itemView.context,
            lifecycleOwner
        ).blurByUrl(itemEntity.strThumb) { blurBitmap ->
            mDrinkImage.setImageBitmap(blurBitmap)
        }
        mDrinkText.text = itemEntity.strDrink

        itemView.setOnClickListener {
            itemObserver?.onItemClicked(itemEntity)
        }
    }
}