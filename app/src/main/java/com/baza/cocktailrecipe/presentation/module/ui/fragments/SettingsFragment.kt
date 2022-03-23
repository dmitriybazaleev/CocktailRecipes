package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.baza.cocktailrecipe.databinding.FragmentSettingsBinding
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel by viewModels<SettingsViewModel>()

    private var mEventJob: Job? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStateObserver()
        addListener()
        initEventObserver()
    }

    private fun initEventObserver() {
        mEventJob = viewModel.settingsEvent
            .onEach { event ->
            }
            .launchIn(lifecycleScope)
    }

    private fun addListener() {
        binding?.scNightMode?.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateNightMode(isChecked)
        }
    }

    private fun addStateObserver() {
        viewModel.settingsLiveData.observe(viewLifecycleOwner) { state ->
            if (binding?.scNightMode?.isChecked != state.isNightMode) {
                binding?.scNightMode?.isChecked = state.isNightMode
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mEventJob?.cancel()
    }
}