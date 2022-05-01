package com.baza.cocktailrecipe.di.navigation

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides

@Module
class FragmentModule constructor(
    private val fragmentActivity: FragmentActivity
) {

    @Provides
    @ActivityScope
    fun provideFragmentManager(): FragmentManager = fragmentActivity.supportFragmentManager
}