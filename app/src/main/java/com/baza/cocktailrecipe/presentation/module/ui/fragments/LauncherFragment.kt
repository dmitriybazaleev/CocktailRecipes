package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.databinding.FragmentLauncherBinding
import com.baza.cocktailrecipe.presentation.module.ui.event.LauncherEvent
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.LauncherViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

const val DEFAULT_SPLASH_DELAY = 2000L

class LauncherFragment : BaseFragment<FragmentLauncherBinding>() {

    private val viewModel by viewModels<LauncherViewModel>()

    private var jobEvent: Job? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLauncherBinding
        get() = FragmentLauncherBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerViewEvent()
        observeState()
    }

    private fun observeState() {
        viewModel.launcherState.observe(viewLifecycleOwner) { state ->
            updateProgressState(state.isShowProgress)
        }
    }

    private fun updateProgressState(isShow: Boolean) {
        binding?.pbLauncher?.isVisible = isShow
    }

    private fun observerViewEvent() {
        jobEvent = viewModel.launcherEvent
            .onEach { event ->
                when (event) {
                    is LauncherEvent.NavEvent -> {
                        act?.navigator?.addFragment(event.destinationId)
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        jobEvent?.cancel()
    }

    override fun isShowBottomNavigation(): Boolean = false
}