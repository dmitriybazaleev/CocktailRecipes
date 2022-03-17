package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.LabelUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkUiEntitySearch
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.SearchUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.SearchHolder
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.SearchResultHolder
import java.lang.IllegalStateException

fun List<DrinkEntity>.mapSearchEntity(): List<SearchUiEntity> {
    val newList = mutableListOf<SearchUiEntity>()
    newList.add(LabelUiEntity("Результат поиска"))

    if (this.isNotEmpty()) {
        this.forEach { dataEntity ->
            newList.add(
                DrinkUiEntitySearch(
                    idDrink = dataEntity.idDrink,
                    strDrink = dataEntity.strDrink,
                    strCategory = dataEntity.strCategory,
                    strAlcoholic = dataEntity.strAlcoholic,
                    strGlass = dataEntity.strGlass,
                    strInstruction = dataEntity.strInstruction,
                    strDrinkThumb = dataEntity.strDrinkThumb,
                    strVideo = dataEntity.strVideo
                )
            )
        }
    }

    return newList
}

private fun SearchUiEntity.compare(newItem: SearchUiEntity): Boolean {
    return when (newItem.getViewType()) {
        SearchByNameAdapter.SearchViewType.SEARCH_TEXT_TYPE -> {
            val newModel = newItem as? LabelUiEntity
            val oldModel = this as? LabelUiEntity
            newModel?.searchLabel == oldModel?.searchLabel
        }
        SearchByNameAdapter.SearchViewType.SEARCH_RESULT_TYPE -> {
            val newModel = newItem as? DrinkUiEntitySearch
            val oldModel = this as? DrinkUiEntitySearch
            (newModel?.idDrink == oldModel?.idDrink
                    && newModel?.strDrink == oldModel?.strDrink
                    && newModel?.strCategory == oldModel?.strCategory)
        }
    }
}

class SearchByNameAdapter constructor(
    private val itemObserver: SearchHolder.ItemObserver? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val searchDiffCallback = object : DiffUtil.ItemCallback<SearchUiEntity>() {
        override fun areItemsTheSame(
            oldItem: SearchUiEntity,
            newItem: SearchUiEntity
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: SearchUiEntity,
            newItem: SearchUiEntity
        ): Boolean {
            return oldItem.compare(newItem)
        }

    }

    fun updateList(searchResult: List<SearchUiEntity>) =
        mSearchAsyncDiffer.submitList(searchResult)

    private val mSearchAsyncDiffer: AsyncListDiffer<SearchUiEntity> =
        AsyncListDiffer(this, searchDiffCallback)

    fun getCurrentList() = mSearchAsyncDiffer.currentList

    override fun getItemViewType(position: Int): Int =
        mSearchAsyncDiffer.currentList[position].getViewType().ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SearchViewType.SEARCH_RESULT_TYPE.ordinal -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_search_result, parent, false)

                SearchHolder(v, itemObserver)
            }
            SearchViewType.SEARCH_TEXT_TYPE.ordinal -> {
                SearchResultHolder.create(parent)
            }

            else -> throw IllegalStateException("Unknown view type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchHolder -> {
                holder.bind(mSearchAsyncDiffer.currentList[position] as DrinkUiEntitySearch)
            }
            is SearchResultHolder -> {
                holder.bind(mSearchAsyncDiffer.currentList[position] as LabelUiEntity)
            }
        }
    }

    override fun getItemCount(): Int = mSearchAsyncDiffer.currentList.size


    enum class SearchViewType {
        /**
         * Результат поиска того, что пришло с сервера
         */
        SEARCH_RESULT_TYPE,

        /**
         * Данный тип будет просто показывать View "Результат поиска"
         */
        SEARCH_TEXT_TYPE,
    }
}
