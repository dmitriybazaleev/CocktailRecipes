package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.databinding.FragmentHomeBinding
import com.baza.cocktailrecipe.presentation.module.ui.dialog.ActionDialog
import com.baza.cocktailrecipe.presentation.module.ui.event.HomeEvent
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.HomeAdapter
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

const val TAG = "homeTag"

class HomeFragment : BaseFragment<FragmentHomeBinding>(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by viewModels<HomeViewModel>()

    private var mAdapter: HomeAdapter? = null
    private var eventJob: Job? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addStateObserver()
        observeEvent()
        addListeners()
        initRecycler()
    }

    private fun initRecycler() {
        mAdapter = HomeAdapter(lifecycleOwner = viewLifecycleOwner)
        binding?.rvHome?.adapter = mAdapter
    }

    private fun addListeners() {
        binding?.slHome?.setOnRefreshListener(this)
    }

    private fun addStateObserver() {
        viewModel.homeLiveData.observe(viewLifecycleOwner) { state ->
            updateProgressState(state.isShowProgress)
            updateSwipeState(state.isRefreshing)
            mAdapter?.updateList(state.cocktailsList)
        }
    }

    private fun updateSwipeState(isSwiped: Boolean) {
        binding?.slHome?.isRefreshing = isSwiped
    }

    private fun observeEvent() {
        eventJob = viewModel.homeEvent
            .onEach { event ->
                when (event) {
                    is HomeEvent.NetworkError -> {
                        ActionDialog.Builder(requireContext(), childFragmentManager)
                            .setLabel(event.title)
                            .setMessage(event.message)
                            .setNegativeButton(R.string.ok)
                            .setIcon(R.drawable.icn_error)
                            .show()
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    private fun updateProgressState(isShow: Boolean) {
        binding?.pbHome?.isVisible = isShow
    }

    override fun onRefresh() {
    }
}