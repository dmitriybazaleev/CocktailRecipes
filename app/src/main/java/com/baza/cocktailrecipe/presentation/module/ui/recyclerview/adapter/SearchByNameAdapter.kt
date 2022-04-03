package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity
import com.baza.cocktailrecipe.presentation.module.ui.dp
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.LabelUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkUiEntitySearch
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.SearchNameUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.SearchCocktailHolder
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.LabelHolder
import com.baza.cocktailrecipe.presentation.module.ui.sp
import java.lang.IllegalStateException

fun List<DrinkEntity>.toSearchViewType(
    labelStr: String,
    includeSwipe: Boolean
): MutableList<SearchNameUiEntity> {
    val newList = mutableListOf<SearchNameUiEntity>()

    if (this.isNotEmpty()) {
        newList.add(LabelUiEntity(labelStr))

        this.forEach { dataEntity ->
            newList.add(
                DrinkUiEntitySearch(
                    idDrink = dataEntity.drinkId,
                    strDrink = dataEntity.strDrink,
                    strCategory = dataEntity.strCategory,
                    strAlcoholic = dataEntity.strAlcoholic,
                    strGlass = dataEntity.strGlass,
                    strInstruction = dataEntity.strInstruction,
                    strDrinkThumb = dataEntity.strDrinkThumb,
                    strVideo = dataEntity.strVideo,
                    isSavedList = includeSwipe
                )
            )
        }
    }

    return newList
}

fun List<IngredientEntity>.toSearchType(
    label: String,
    includeSwipe: Boolean
): List<SearchNameUiEntity> {
    val listResult = mutableListOf<SearchNameUiEntity>()

    if (this.isNotEmpty()) {
        listResult.add(
            LabelUiEntity(
                label
            )
        )

        this.forEach { ingredientEntity ->
            listResult.add(
                DrinkUiEntitySearch(
                    idDrink = ingredientEntity.idIngredientStr,
                    strDrink = ingredientEntity.strIngredient,
                    strCategory = ingredientEntity.strType,
                    strAlcoholic = ingredientEntity.strAlcohol,
                    strGlass = null,
                    strInstruction = ingredientEntity.strDescription,
                    strDrinkThumb = null,
                    strVideo = null,
                    isSavedList = includeSwipe
                )
            )
        }
    }


    return listResult
}

fun DrinkUiEntitySearch.toDrinkEntity(): DrinkEntity {
    return DrinkEntity(
        drinkId = idDrink,
        strDrink = strDrink,
        strCategory = strCategory,
        strAlcoholic = strAlcoholic,
        strGlass = strGlass,
        strInstruction = strInstruction,
        strDrinkThumb = strDrinkThumb,
        strVideo = strVideo
    )
}

fun DrinkUiEntitySearch.toIngredientEntity(): IngredientEntity {
    return IngredientEntity(
        idIngredientStr = idDrink,
        strIngredient = strDrink,
        strDescription = strInstruction,
        strType = strGlass,
        strAlcohol = strAlcoholic,
        strAbv = null
    )
}

private fun SearchNameUiEntity.compare(newItem: SearchNameUiEntity): Boolean {
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
    private val itemObserver: SearchCocktailHolder.ItemObserver? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val searchDiffCallback = object : DiffUtil.ItemCallback<SearchNameUiEntity>() {
        override fun areItemsTheSame(
            oldItem: SearchNameUiEntity,
            newItem: SearchNameUiEntity
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: SearchNameUiEntity,
            newItem: SearchNameUiEntity
        ): Boolean = oldItem.compare(newItem)

    }

    private fun getItemTouchCallback(
        callback: ((item: DrinkUiEntitySearch, holder: RecyclerView.ViewHolder) -> Unit)? = null
    ): ItemTouchHelper.SimpleCallback {

        return object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                if (viewHolder is LabelHolder) return 0
                if (viewHolder is SearchCocktailHolder) {
                    try {
                        val itemEntity =
                            mSearchAsyncDiffer.currentList[viewHolder.adapterPosition] as? DrinkUiEntitySearch

                        if (itemEntity?.isSavedList == false) {
                            return 0
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                return super.getMovementFlags(recyclerView, viewHolder)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (viewHolder is SearchCocktailHolder) {
                    callback?.invoke(
                        mSearchAsyncDiffer.currentList[viewHolder.adapterPosition] as DrinkUiEntitySearch,
                        viewHolder
                    )
                }
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val item = viewHolder.itemView
                    val mColorDrawable = ColorDrawable(Color.RED)
                    mColorDrawable.setBounds(
                        item.left,
                        item.top, item.right, item.bottom
                    )
                    mColorDrawable.draw(c)

                    val x = (item.right - 60.dp).toFloat()
                    val y = (item.top + item.height / 1.8).toFloat()
                    c.drawText(
                        item.context.getString(R.string.remove),
                        x,
                        y,
                        getDefaultPaintStyle()
                    )
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
    }

    private fun getDefaultPaintStyle(): Paint {
        val paint = Paint()
        paint.color = Color.WHITE
        paint.typeface = Typeface.create("san-serif-medium", Typeface.NORMAL)
        paint.textSize = 14.sp.toFloat()

        return paint
    }

    private val mSearchAsyncDiffer: AsyncListDiffer<SearchNameUiEntity> =
        AsyncListDiffer(this, searchDiffCallback)
    private var mItemTouchHelper: ItemTouchHelper? = null


    override fun getItemViewType(position: Int): Int =
        mSearchAsyncDiffer.currentList[position].getViewType().ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SearchViewType.SEARCH_RESULT_TYPE.ordinal -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_search_result, parent, false)

                SearchCocktailHolder(v, itemObserver)
            }
            SearchViewType.SEARCH_TEXT_TYPE.ordinal -> {
                LabelHolder.create(parent)
            }

            else -> throw IllegalStateException("Unknown view type!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SearchCocktailHolder -> {
                holder.bind(mSearchAsyncDiffer.currentList[position] as DrinkUiEntitySearch)
            }
            is LabelHolder -> {
                holder.bind(mSearchAsyncDiffer.currentList[position] as LabelUiEntity)
            }
        }
    }

    override fun getItemCount(): Int = mSearchAsyncDiffer.currentList.size

    fun attachItemTouch(
        recyclerView: RecyclerView?,
        itemCallback: ((item: DrinkUiEntitySearch, holder: RecyclerView.ViewHolder) -> Unit)? = null,
    ) {
        if (mItemTouchHelper == null) {
            mItemTouchHelper = ItemTouchHelper(getItemTouchCallback(itemCallback))
            mItemTouchHelper?.attachToRecyclerView(recyclerView)
        }
    }

    fun updateList(searchResult: List<SearchNameUiEntity>) =
        mSearchAsyncDiffer.submitList(searchResult)

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
