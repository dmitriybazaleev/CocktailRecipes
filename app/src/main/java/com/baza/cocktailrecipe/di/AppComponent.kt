package com.baza.cocktailrecipe.di

import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.HomeViewModel
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SavedViewModel
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SearchByIngredientViewModel
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.SearchByNameViewModel
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        RetrofitModule::class,
        DataBaseModule::class,
        UseCaseModule::class,
        AppModule::class,
        ApiModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(homeViewModel: HomeViewModel)
    fun inject(homeViewModel: SearchByNameViewModel)
    fun inject(searchByIngredientViewModel: SearchByIngredientViewModel)
    fun inject(savedViewModel: SavedViewModel)
}