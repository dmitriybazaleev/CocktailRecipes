package com.baza.cocktailrecipe.di

import android.content.Context
import com.baza.cocktailrecipe.presentation.base.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(
    private val app: App
) {

    @Provides
    @Singleton
    fun provideApp(): Context = app.applicationContext
}