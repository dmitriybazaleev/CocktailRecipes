package com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.baza.cocktailrecipe.presentation.module.ui.fragments.SearchByIngredientFragment
import com.baza.cocktailrecipe.presentation.module.ui.fragments.SearchByNameFragment
import java.lang.IllegalStateException

class SearchAdapter constructor(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    companion object {
        const val MAX_FRAGMENTS = 2

        const val SEARCH_BY_NAME_POSITION = 0
        const val SEARCH_BY_INGREDIENT_POSITION = 1
    }

    override fun getItemCount(): Int = MAX_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            SEARCH_BY_NAME_POSITION -> {
                SearchByNameFragment.create()
            }
            SEARCH_BY_INGREDIENT_POSITION ->
                SearchByIngredientFragment.create()

            else -> throw IllegalStateException("Unknown position")
        }
    }
}