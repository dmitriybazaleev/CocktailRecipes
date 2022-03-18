package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.ui.dp
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.LabelUiEntity

class LabelHolder constructor(
    private val itemText: TextView
) : RecyclerView.ViewHolder(itemText) {

    companion object {

        @JvmStatic
        fun create(viewGroup: ViewGroup): LabelHolder {
            val labelText = TextView(viewGroup.context)
            val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            labelText.id = ViewCompat.generateViewId()
            labelText.setTextColor(ContextCompat.getColor(viewGroup.context, R.color.black))
            labelText.setPadding(0, 10.dp, 0, 10.dp)
            labelText.layoutParams = lp

            labelText.typeface = Typeface.create(
                "sans-serif-medium", Typeface.NORMAL
            )
            labelText.textSize = 16f

            return LabelHolder(labelText)
        }
    }

    fun bind(entity: LabelUiEntity) {
        itemText.text = entity.searchLabel
    }
}