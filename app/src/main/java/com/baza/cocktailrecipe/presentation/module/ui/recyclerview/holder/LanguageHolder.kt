package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder

import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.LanguageUiEntity

class LanguageHolder constructor(
    itemView: View,
    private val languageObserver: LanguageObserver? = null
) : RecyclerView.ViewHolder(itemView) {

    private val mLanguageName = itemView.findViewById<TextView>(R.id.txv_language_name)
    private val mLanguageNative = itemView.findViewById<TextView>(R.id.txv_language_native);
    private val mLanguageRadioButton = itemView.findViewById<RadioButton>(R.id.rb_language_select)


    fun bind(entity: LanguageUiEntity) {
        mLanguageName.text = entity.name
        mLanguageNative.text = entity.nativeName
        mLanguageRadioButton.isChecked = entity.isLanguageSelected

        itemView.setOnClickListener {
            languageObserver?.onCheckChanged(true, adapterPosition, entity)
        }

        mLanguageRadioButton.setOnClickListener {
            languageObserver?.onCheckChanged(true, adapterPosition, entity)
        }
    }

    interface LanguageObserver {
        fun onCheckChanged(isChecked: Boolean, position: Int, entity: LanguageUiEntity)
    }
}