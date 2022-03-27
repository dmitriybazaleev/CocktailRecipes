package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.IngredientUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.SearchIngredientUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.IngredientHolder
import java.lang.IllegalStateException

// TODO: 26.03.2022 Дописать логику
private fun SearchIngredientUiEntity.compare(newItem: SearchIngredientUiEntity): Boolean {
    return when (newItem.getIngredientViewType()) {
        SearchIngredientAdapter.IngredientViewType.LABEL_INGREDIENT_TYPE -> {
            val oldModel = this as? IngredientUiEntity
            val newModel = newItem as? IngredientUiEntity

            return oldModel?.strIngredient == newModel?.strIngredient
                    && oldModel?.strType == newModel?.strType
                    && oldModel?.strDescription == newModel?.strDescription
        }


        else -> true
    }
}

class SearchIngredientAdapter constructor(
    private val ingredientItemObserver: IngredientHolder.IngredientItemObserver? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mIngredientDifferCallback =
        object : DiffUtil.ItemCallback<SearchIngredientUiEntity>() {
            override fun areItemsTheSame(
                oldItem: SearchIngredientUiEntity,
                newItem: SearchIngredientUiEntity
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: SearchIngredientUiEntity,
                newItem: SearchIngredientUiEntity
            ): Boolean = oldItem.compare(newItem)

        }

    private val mIngredientDiffer = AsyncListDiffer(
        this,
        mIngredientDifferCallback
    )

    fun getCurrentList() = mIngredientDiffer.currentList

    fun updateList(newData: List<SearchIngredientUiEntity>) =
        mIngredientDiffer.submitList(newData)

    fun getItemByPosition(position: Int): Any? {
        return try {
            getCurrentList()[position]

        } catch (e: Exception) {
            null
        }
    }

    override fun getItemViewType(position: Int): Int =
        getCurrentList()[position].getIngredientViewType().ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            IngredientViewType.INGREDIENT_TYPE.ordinal -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ingredient, parent, false)

                IngredientHolder(view)
            }

            else -> throw IllegalStateException("Unknown view type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IngredientHolder -> {
                holder.bind(getCurrentList()[position] as IngredientUiEntity)
            }
        }
    }

    override fun getItemCount(): Int = getCurrentList().size

    enum class IngredientViewType {

        /**
         * Данный тип будет показывать Label "Результат поиска или "
         */
        LABEL_INGREDIENT_TYPE,

        /**
         * Данный тип будет отображать результат поиска ингредиента
         */
        INGREDIENT_TYPE
    }
}