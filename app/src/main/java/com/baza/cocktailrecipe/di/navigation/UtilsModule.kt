package com.baza.cocktailrecipe.di.navigation

import android.content.Context
import com.baza.cocktailrecipe.presentation.module.ui.blur.BlurHelperImpl
import com.baza.cocktailrecipe.presentation.module.ui.blur.IBlurHelper
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {

    @Provides
    @ActivityScope
    fun provideBlurHelper(appContext: Context) : IBlurHelper {
        return BlurHelperImpl(appContext)
    }
}