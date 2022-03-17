package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.HomeUiEntity

private fun HomeUiEntity.compare(newItem: HomeUiEntity): Boolean {
    return false
}

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = mAsyncHomeDiffer.currentList.size

    enum class HomeViewType {

    }
}