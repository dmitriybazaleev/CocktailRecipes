package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.baza.cocktailrecipe.databinding.FragmentSelectLanguageBinding
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.LanguagesAdapter
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.LanguageUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.holder.LanguageHolder
import com.baza.cocktailrecipe.presentation.module.ui.activity.setAppLanguage
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SelectLanguageViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SelectLanguageFragment : BaseFragment<FragmentSelectLanguageBinding>(),
    LanguageHolder.LanguageObserver {

    private val viewModel by viewModels<SelectLanguageViewModel>()
    private val mLanguageAdapter = LanguagesAdapter(this)

    override val bindingInflater: (
        LayoutInflater, ViewGroup?,
        Boolean
    ) -> FragmentSelectLanguageBinding
        get() = FragmentSelectLanguageBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWithBaseViewModel(viewModel)
        setUpRecycler()
        initObserver()
        observeViewEvent()
        initListeners()
    }

    private fun initListeners() {
        binding?.tbSelectLanguage?.setOnBackButtonClick {
            popBackstack()
        }

        binding?.btnSelectLanguage?.setOnClickListener {
            viewModel.updatePrefLanguage()
            act?.setAppLanguage(viewModel.mSelectedCode)
            popBackstack()
        }
    }

    private fun updateButtonEnabled(isEnabled: Boolean) {
        binding?.btnSelectLanguage?.isEnabled = isEnabled
    }

    private fun observeViewEvent() {
        viewModel.selectLanguageEvent
            .onEach { event ->
            }
            .launchIn(lifecycleScope)
    }

    private fun initObserver() {
        viewModel.selectLanguagesLiveData.observe(viewLifecycleOwner) { state ->
            mLanguageAdapter.updateList(state.languagesList)
            updateButtonEnabled(state.isButtonEnabled)
        }
    }

    private fun setUpRecycler() {
        binding?.rvSelectLanguage?.adapter = mLanguageAdapter
    }

    override fun onCheckChanged(isChecked: Boolean, position: Int, entity: LanguageUiEntity) {
        viewModel.onCheckChanged(isChecked, position, entity)
    }
}