package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.CocktailHolder

private fun DrinkEntity.compare(newItem: DrinkEntity): Boolean {
    return this.strDrink == newItem.strDrink
            && this.strCategory == newItem.strCategory
            && this.strInstruction == newItem.strInstruction
}

class RandomCocktailAdapter constructor(
    private val itemObserver: CocktailHolder.CocktailItemObserver? = null
) : RecyclerView.Adapter<CocktailHolder>() {

    private val cocktailDiffCallback = object : DiffUtil.ItemCallback<DrinkEntity>() {
        override fun areItemsTheSame(
            oldItem: DrinkEntity,
            newItem: DrinkEntity
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DrinkEntity,
            newItem: DrinkEntity
        ): Boolean = oldItem.compare(newItem)

    }

    private val randomCocktailDiffer = AsyncListDiffer(
        this,
        cocktailDiffCallback
    )

    fun getCurrentList() = randomCocktailDiffer.currentList

    fun getItemByPosition(position: Int): DrinkEntity? =
        try {
            getCurrentList()[position]

        } catch (e: Exception) {
            null
        }

    fun updateList(newData: List<DrinkEntity>) = randomCocktailDiffer.submitList(newData)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cocktail_by_letter, parent, false)

        return CocktailHolder(view, itemObserver)
    }

    override fun onBindViewHolder(holder: CocktailHolder, position: Int) {
        holder.bind(getCurrentList()[position])
    }

    override fun getItemCount(): Int = getCurrentList().size
}