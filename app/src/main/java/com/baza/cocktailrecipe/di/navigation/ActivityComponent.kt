package com.baza.cocktailrecipe.di.navigation

import com.baza.cocktailrecipe.presentation.module.ui.activity.MainActivity
import dagger.Subcomponent

@Subcomponent(
    modules = [FragmentModule::class, NavigationModule::class],
)
@ActivityScope
interface ActivityComponent {

    fun inject(act: MainActivity)

    @Subcomponent.Builder
    interface ActivityBuilder {

        fun setFragmentModule(fragmentModule: FragmentModule): ActivityBuilder

        fun build(): ActivityComponent
    }
}