package com.baza.cocktailrecipe.presentation.module.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.databinding.ActivityMainBinding
import com.baza.cocktailrecipe.presentation.navigation.attachController

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLightStatusBar()
        attachController()
    }

    private fun attachController() {
        binding?.bnvMain?.attachController(navigator?.getController())
    }

    fun isShowBottomNav(isShow: Boolean) {
        if (binding?.bnvMain?.isVisible != isShow) {
            binding?.bnvMain?.isVisible = isShow
        }
    }

    private fun initLightStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            window.navigationBarColor = Color.BLACK
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                window.statusBarColor =
                    ContextCompat.getColor(applicationContext, R.color.status_bar_semi_transparent)
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            window.navigationBarColor = Color.WHITE
            window.statusBarColor = Color.WHITE
        }
    }

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate
}