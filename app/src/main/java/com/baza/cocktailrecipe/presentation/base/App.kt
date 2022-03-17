package com.baza.cocktailrecipe.presentation.base

import android.app.Application
import com.baza.cocktailrecipe.di.AppComponent
import com.baza.cocktailrecipe.di.AppModule
import com.baza.cocktailrecipe.di.DaggerAppComponent

class App : Application() {

    companion object {
        var appComponent: AppComponent? = null
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
        app = this
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}