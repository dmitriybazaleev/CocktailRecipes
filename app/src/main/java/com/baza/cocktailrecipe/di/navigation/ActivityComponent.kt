package com.baza.cocktailrecipe.di.navigation

import com.baza.cocktailrecipe.presentation.module.ui.activity.MainActivity
import com.baza.cocktailrecipe.presentation.module.ui.fragments.HomeFragment
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.HomeViewModel
import dagger.Subcomponent

@Subcomponent(
    modules = [FragmentModule::class, NavigationModule::class, UtilsModule::class],
)
@ActivityScope
interface ActivityComponent {

    fun inject(act: MainActivity)
    fun inject(homeFragment: HomeFragment)

    @Subcomponent.Builder
    interface ActivityBuilder {

        fun setFragmentModule(fragmentModule: FragmentModule): ActivityBuilder

        fun build(): ActivityComponent
    }
}