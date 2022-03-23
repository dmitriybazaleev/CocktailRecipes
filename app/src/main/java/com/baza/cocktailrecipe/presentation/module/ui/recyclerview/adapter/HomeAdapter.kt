package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkByLetterUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.HomeUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.RandomDrinkUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.CocktailByLetterHolder
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.ItemObserver
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.RandomCocktailHolder
import java.lang.IllegalStateException

private fun HomeUiEntity.compare(newItem: HomeUiEntity): Boolean {
    return when (newItem.getHomeState()) {
        HomeAdapter.HomeViewType.COCKTAIL_BY_LETTER_TYPE -> {
            val oldModel = this as? DrinkByLetterUiEntity
            val newModel = newItem as? DrinkByLetterUiEntity

            oldModel?.strDrink == newModel?.strDrink
                    && oldModel?.strCategory == newModel?.strCategory
                    && oldModel?.strInstruction == newModel?.strInstruction
        }
        HomeAdapter.HomeViewType.RANDOM_COCKTAIL_TYPE -> {
            val oldModel = this as? RandomDrinkUiEntity
            val newModel = newItem as? RandomDrinkUiEntity

            oldModel?.idDrink == newModel?.idDrink
                    && oldModel?.strDrink == newModel?.strDrink
                    && oldModel?.strThumb == newModel?.strThumb
        }
    }
}

fun List<DrinkEntity>.toBlurViewType(): List<HomeUiEntity> {
    val listResult = mutableListOf<HomeUiEntity>()
    if (this.isNotEmpty()) {
        this.forEach { entity ->
            listResult.add(
                RandomDrinkUiEntity(
                    idDrink = "0", // TODO: 23.03.2022 Обязательно сделать нормальный id
                    strDrink = entity.strDrink,
                    strThumb = entity.strDrinkThumb
                )
            )
        }
    }

    return listResult
}

fun List<DrinkEntity>.toRandomDrink(): List<HomeUiEntity> {
    val listResult = mutableListOf<HomeUiEntity>()
    if (this.isNotEmpty()) {
        this.forEach { argument ->
            listResult.add(
                DrinkByLetterUiEntity(
                    strDrink = argument.strDrink,
                    strCategory = argument.strCategory,
                    strAlcoholic = argument.strAlcoholic,
                    strGlass = argument.strGlass,
                    strInstruction = argument.strInstruction,
                    strDrinkThumb = argument.strDrinkThumb
                )
            )
        }
    }


    return listResult
}

class HomeAdapter constructor(
    private val lifecycleOwner: LifecycleOwner,
    private val randomCocktailObserver: ItemObserver<RandomDrinkUiEntity>? = null,
    private val cocktailByLetterObserver: ItemObserver<DrinkByLetterUiEntity>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mDiffUtilCallback = object : DiffUtil.ItemCallback<HomeUiEntity>() {
        override fun areItemsTheSame(oldItem: HomeUiEntity, newItem: HomeUiEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: HomeUiEntity, newItem: HomeUiEntity): Boolean {
            return oldItem.compare(newItem)
        }

    }

    fun updateList(newList: List<HomeUiEntity>) = mAsyncHomeDiffer.submitList(newList)

    private val mAsyncHomeDiffer = AsyncListDiffer(this, mDiffUtilCallback)

    fun getCurrentList() = mAsyncHomeDiffer.currentList

    override fun getItemViewType(position: Int): Int =
        mAsyncHomeDiffer.currentList[position].getHomeState().ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            HomeViewType.RANDOM_COCKTAIL_TYPE.ordinal -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_random_cocktail, parent, false)

                RandomCocktailHolder(view, itemObserver = randomCocktailObserver)
            }
            HomeViewType.COCKTAIL_BY_LETTER_TYPE.ordinal -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_cocktail_by_letter, parent, false)

                CocktailByLetterHolder(view, itemObserver = cocktailByLetterObserver)
            }

            else -> throw IllegalStateException("Unknown view type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CocktailByLetterHolder -> {
                holder.bind(
                    mAsyncHomeDiffer.currentList[position] as DrinkByLetterUiEntity,

                    )
            }
            is RandomCocktailHolder -> {
                holder.bind(
                    mAsyncHomeDiffer.currentList[position] as RandomDrinkUiEntity,
                    lifecycleOwner,

                    )
            }
        }
    }

    override fun getItemCount(): Int = mAsyncHomeDiffer.currentList.size

    enum class HomeViewType {
        /**
         * Рандомный тип коктейля.
         * Данный holder будет блюрить item
         */
        RANDOM_COCKTAIL_TYPE,

        /**
         * Данный тип будет показывать коктейл по первой букве
         */
        COCKTAIL_BY_LETTER_TYPE
    }
}