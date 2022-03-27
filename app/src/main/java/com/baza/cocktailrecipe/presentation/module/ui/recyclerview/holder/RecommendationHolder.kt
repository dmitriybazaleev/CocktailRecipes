package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.RecommendationUiEntity

class RecommendationHolder constructor(
    itemView: View,
    private val itemObserver: RecommendationItemObserver? = null
) : RecyclerView.ViewHolder(itemView) {

    private val recommendationText = itemView.findViewById<TextView>(R.id.txv_recommendation)
    private val cardRoot = itemView.findViewById<CardView>(R.id.cv_recommendation_root)

    fun bind(item: RecommendationUiEntity) {
        recommendationText.text = item.cocktailName
        if (item.isSelected) {
            cardRoot.setCardBackgroundColor(
                ContextCompat.getColor(itemView.context, R.color.pure_red)
            )
            recommendationText.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))

        } else {
            cardRoot.setCardBackgroundColor(
                ContextCompat.getColor(itemView.context, R.color.white)
            )
            recommendationText.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
        }

        itemView.setOnClickListener {
            itemObserver?.onRecommendationItemClicked(item)
        }
    }

    interface RecommendationItemObserver {
        fun onRecommendationItemClicked(entity: RecommendationUiEntity)
    }
}