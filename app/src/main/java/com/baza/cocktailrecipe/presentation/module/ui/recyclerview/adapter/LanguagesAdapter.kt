package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.data.entity.LanguageEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.LanguageUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.LanguageHolder

fun List<LanguageEntity>.toLanguageUiEntity(): MutableList<LanguageUiEntity> {
    val listResult = mutableListOf<LanguageUiEntity>()

    if (this.isNotEmpty()) {
        this.forEach { language ->
            listResult.add(
                LanguageUiEntity(
                    name = language.name,
                    nativeName = language.nativeName,
                    code = language.code,
                    isLanguageSelected = false
                )
            )
        }
    }

    return listResult
}

fun LanguageEntity.toLanguageUiEntity(
    isSelected: Boolean
): LanguageUiEntity {
    return LanguageUiEntity(
        name = this.name,
        nativeName = this.nativeName,
        code = this.code,
        isLanguageSelected = isSelected
    )
}

class LanguagesAdapter constructor(
    private val itemObserver: LanguageHolder.LanguageObserver? = null
) : RecyclerView.Adapter<LanguageHolder>() {

    private var languagesList = listOf<LanguageUiEntity>()

    private fun getDiffCallback(newList: List<LanguageUiEntity>): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = languagesList.size

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean = languagesList[oldItemPosition] == newList[newItemPosition]

            override fun areContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean = languagesList[oldItemPosition].name == newList[newItemPosition].name
                    && languagesList[oldItemPosition].nativeName == newList[newItemPosition].nativeName
                    && languagesList[oldItemPosition].code == newList[newItemPosition].code
                    && languagesList[oldItemPosition].isLanguageSelected !=
                    newList[newItemPosition].isLanguageSelected

        }
    }


    fun updateList(newList: List<LanguageUiEntity>) {
        val diffUtil = DiffUtil.calculateDiff(getDiffCallback(newList))
        languagesList = newList
        diffUtil.dispatchUpdatesTo(this)
    }

    fun getItemByPosition(position: Int): LanguageUiEntity? {
        return try {
            languagesList[position]

        } catch (e: Exception) {
            null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language, parent, false)

        return LanguageHolder(view, itemObserver)
    }

    override fun onBindViewHolder(holder: LanguageHolder, position: Int) {
        holder.bind(languagesList[position])
    }

    override fun getItemCount(): Int = languagesList.size
}