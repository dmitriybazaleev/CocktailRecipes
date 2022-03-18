package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.databinding.FragmentSearchBinding
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.SearchAdapter
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SearchViewModel
import com.google.android.material.tabs.TabLayoutMediator

const val DEFAULT_TEXT_DELAY = 800L

class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel by viewModels<SearchViewModel>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSearchBinding
        get() = FragmentSearchBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        binding?.vpSearch?.adapter = SearchAdapter(this)
        TabLayoutMediator(
            binding?.tlSearch ?: return,
            binding?.vpSearch ?: return
        ) { tab, position ->
            when (position) {
                0 -> tab.setText(R.string.by_name)
                1 -> tab.setText(R.string.by_ingredient)
            }
        }.attach()
    }
}