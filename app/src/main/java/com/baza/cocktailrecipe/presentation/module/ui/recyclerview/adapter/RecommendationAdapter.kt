package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.RecommendationUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.RecommendationHolder

private fun RecommendationUiEntity.compare(newItem: RecommendationUiEntity): Boolean {
    return this.cocktailName == newItem.cocktailName
}

fun List<DrinkEntity>.toRecommendationEntity(): List<RecommendationUiEntity> {
    val listResult = mutableListOf<RecommendationUiEntity>()

    this.forEach { drinkEntity ->
        listResult.add(
            RecommendationUiEntity(
                cocktailName = drinkEntity.strDrink,
                isSelected = false
            )
        )
    }

    return listResult
}

class RecommendationAdapter constructor(
    private val itemObserver: RecommendationHolder.RecommendationItemObserver? = null
) : RecyclerView.Adapter<RecommendationHolder>() {

    private val recommendationCallback = object :
        DiffUtil.ItemCallback<RecommendationUiEntity>() {
        override fun areItemsTheSame(
            oldItem: RecommendationUiEntity,
            newItem: RecommendationUiEntity
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: RecommendationUiEntity,
            newItem: RecommendationUiEntity
        ): Boolean = oldItem.compare(newItem)

    }

    private val recommendationDiffer = AsyncListDiffer(
        this,
        recommendationCallback
    )

    fun getCurrentList() = recommendationDiffer.currentList

    fun getItemByPosition(position: Int): RecommendationUiEntity? =
        try {
            recommendationDiffer.currentList[position]

        } catch (e: Exception) {
            null
        }

    fun updateRecommendations(newList: List<RecommendationUiEntity>) =
        recommendationDiffer.submitList(newList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)

        return RecommendationHolder(view, itemObserver)
    }

    override fun onBindViewHolder(holder: RecommendationHolder, position: Int) {
        holder.bind(getCurrentList()[position])
    }

    override fun getItemCount(): Int = getCurrentList().size
}