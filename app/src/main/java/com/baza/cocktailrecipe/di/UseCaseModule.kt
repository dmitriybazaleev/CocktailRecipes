package com.baza.cocktailrecipe.di

import com.baza.cocktailrecipe.presentation.module.data.api.CocktailApi
import com.baza.cocktailrecipe.presentation.module.data.room.DrinksDao
import com.baza.cocktailrecipe.presentation.module.data.room.IngredientDao
import com.baza.cocktailrecipe.presentation.module.domain.HomeUseCase
import com.baza.cocktailrecipe.presentation.module.domain.SavedUseCase
import com.baza.cocktailrecipe.presentation.module.domain.SearchByNameUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun provideHomeUseCase(
        api: CocktailApi
    ): HomeUseCase = HomeUseCase(api)

    @Provides
    fun provideSearchByNameUseCase(
        api: CocktailApi,
        dao: DrinksDao
    ): SearchByNameUseCase = SearchByNameUseCase(api, dao)


    @Provides
    fun provideSavedUseCase(
        drinksDao: DrinksDao,
        ingredientDao: IngredientDao
    ): SavedUseCase = SavedUseCase(drinksDao, ingredientDao)
}