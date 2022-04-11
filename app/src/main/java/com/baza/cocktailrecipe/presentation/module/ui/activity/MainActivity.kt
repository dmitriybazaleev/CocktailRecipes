package com.baza.cocktailrecipe.presentation.module.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.databinding.ActivityMainBinding
import com.baza.cocktailrecipe.presentation.module.data.PreferencesCache
import com.baza.cocktailrecipe.presentation.module.ui.setAppLanguage
import com.baza.navigation.NavigationController
import com.baza.navigation.Navigator
import com.baza.navigation.attachController

/**
 * Тестовое приложение, которое представляет функционал:
 * 1 - Поиск коктейлей по имени
 * 2 - Лента, которая показывает список рандомных коктейлей
 * 3 - Приложение поддерживает ночной режим
 * Стек, который используется в проекте: MVVM, Kotlin coroutines, Room, Navigation Component,
 * Retrofit, Glide
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    var navigator: Navigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNavigation()
        initLightStatusBar()
        updateAppLanguage()
    }

    private fun createNavigation() {
        navigator = NavigationController(supportFragmentManager, R.id.fcv_main)
        binding?.bnvMain?.attachController(navController = navigator?.getController())
    }

    private fun updateAppLanguage() = setAppLanguage(PreferencesCache.language)

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