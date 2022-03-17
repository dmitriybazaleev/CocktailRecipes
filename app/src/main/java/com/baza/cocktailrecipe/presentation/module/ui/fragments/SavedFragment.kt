package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.baza.cocktailrecipe.databinding.FragmentSavedBinding
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SavedViewModel

class SavedFragment : BaseFragment<FragmentSavedBinding>() {

    private val viewModel by viewModels<SavedViewModel>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSavedBinding
        get() = FragmentSavedBinding::inflate


}