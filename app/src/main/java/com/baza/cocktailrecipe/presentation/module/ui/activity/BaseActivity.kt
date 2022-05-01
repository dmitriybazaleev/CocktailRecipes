package com.baza.cocktailrecipe.presentation.module.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.baza.cocktailrecipe.presentation.base.App

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    private var _binding: B? = null
    protected val app = application as? App

    protected val binding: B?
        get() = _binding


    abstract val bindingInflater: (LayoutInflater) -> B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        _binding = bindingInflater(layoutInflater)
        setContentView(_binding?.root)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}