package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.IngredientUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.SearchIngredientEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.IngredientHolder
import java.lang.IllegalArgumentException

private fun SearchIngredientEntity.compareTo(newItem: SearchIngredientEntity): Boolean {
    return when (newItem.getViewType()) {
        SearchIngredientAdapter.SearchIngredientViewType.SEARCH_ITEM -> {
            val oldModel = this as? IngredientUiEntity
            val newModel = newItem as? IngredientUiEntity

            oldModel?.strIngredient == newModel?.strIngredient
                    && oldModel?.strDescription == newModel?.strDescription
                    && oldModel?.strAlcohol == newModel?.strAlcohol
                    && oldModel?.strType == newModel?.strType
        }
        SearchIngredientAdapter.SearchIngredientViewType.SEARCH_LABEL -> {
            true
        }
    }
}

fun List<IngredientEntity>.toViewType(): List<SearchIngredientEntity> {
    val listResult = mutableListOf<SearchIngredientEntity>()

    this.forEach { ingredient ->
        listResult.add(
            IngredientUiEntity(
                strIngredient = ingredient.strIngredient,
                strDescription = ingredient.strDescription,
                strType = ingredient.strType,
                strAlcohol = ingredient.strAlcohol
            )
        )
    }

    return listResult
}

class SearchIngredientAdapter constructor(
    private val itemObserver: IngredientHolder.ItemObserver? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mSearchIngredientCallback =
        object : DiffUtil.ItemCallback<SearchIngredientEntity>() {
            override fun areItemsTheSame(
                oldItem: SearchIngredientEntity,
                newItem: SearchIngredientEntity
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: SearchIngredientEntity,
                newItem: SearchIngredientEntity
            ): Boolean = oldItem.compareTo(newItem)
        }

    private val mSearchIngredientDiffer = AsyncListDiffer(this, mSearchIngredientCallback)

    fun updateList(newList: List<SearchIngredientEntity>) =
        mSearchIngredientDiffer.submitList(newList)

    fun getList(): List<SearchIngredientEntity> = mSearchIngredientDiffer.currentList

    override fun getItemViewType(position: Int): Int =
        mSearchIngredientDiffer.currentList[position].getViewType().ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        return when (viewType) {
            SearchIngredientViewType.SEARCH_ITEM.ordinal -> {
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ingredient, parent, false)

                IngredientHolder(v, itemObserver)
            }

            SearchIngredientViewType.SEARCH_LABEL.ordinal -> {
                // TODO: 17.03.2022 Пока не знаю 2 View type
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ingredient, parent, false)

                IngredientHolder(v)
            }

            else -> throw IllegalArgumentException("Unknown View type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IngredientHolder -> {
                holder.bind(mSearchIngredientDiffer.currentList[position] as IngredientUiEntity)
            }
        }
    }


    override fun getItemCount(): Int = mSearchIngredientDiffer.currentList.size

    enum class SearchIngredientViewType {
        /**
         * Данный ViewType будет показывать "Результат поиска"
         */
        SEARCH_LABEL,

        /**
         * Item будет показывать результат Ингредиента
         */
        SEARCH_ITEM
    }
}