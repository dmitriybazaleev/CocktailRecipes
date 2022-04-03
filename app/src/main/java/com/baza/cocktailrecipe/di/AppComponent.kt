package com.baza.cocktailrecipe.di

import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.*
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
    fun inject(searchByNameViewModel: SearchByNameViewModel)
    fun inject(savedViewModel: SavedViewModel)
    fun inject(ingredientViewModel: SearchByIngredientViewModel)
    fun inject(selectLanguageViewModel: SelectLanguageViewModel)
}