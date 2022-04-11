package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.databinding.FragmentSettingsBinding
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SettingsViewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel by viewModels<SettingsViewModel>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListener()
    }

    private fun addListener() {
        binding?.txvSelectLanguage?.setOnClickListener {
            addFragment(R.id.action_settingsFragment_to_selectLanguageFragment)
        }
    }
}