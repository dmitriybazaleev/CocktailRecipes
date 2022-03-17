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

    }

    override fun getItemCount(): Int = MAX_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                SearchByNameFragment.create()
            }
            1 -> {
                SearchByIngredientFragment.create()
            }

            else -> throw IllegalStateException("Unknown position")
        }
    }
}