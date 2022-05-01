package com.baza.cocktailrecipe.di.navigation

import androidx.fragment.app.FragmentManager
import com.baza.cocktailrecipe.R
import com.baza.navigation.NavigationController
import com.baza.navigation.Navigator
import dagger.Module
import dagger.Provides

@Module
class NavigationModule {

    @Provides
    @ActivityScope
    fun provideNavigator(
        fragmentManager: FragmentManager
    ): Navigator = NavigationController(fragmentManager, R.id.fcv_main)
}