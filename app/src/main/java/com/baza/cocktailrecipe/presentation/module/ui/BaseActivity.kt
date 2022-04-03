package com.baza.cocktailrecipe.presentation.module.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.baza.cocktailrecipe.presentation.module.ui.fragments.BaseFragment
import com.baza.cocktailrecipe.presentation.navigation.NavigationController
import com.baza.cocktailrecipe.presentation.navigation.Navigator

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    private var _binding: B? = null

    protected val binding: B?
        get() = _binding

    var navigator: Navigator? = null

    abstract val bindingInflater: (LayoutInflater) -> B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initNavigation()
    }

    private fun initNavigation() {
        navigator = NavigationController(supportFragmentManager)
    }

    private fun initView() {
        _binding = bindingInflater(layoutInflater)
        setContentView(_binding?.root)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        val currentFragment = navigator?.getCurrentFragment()
        if (currentFragment is BaseFragment<*>) {
            val handled = currentFragment.onBackPressed()

            if (!handled) {
                super.onBackPressed()
            }
        }
    }
}