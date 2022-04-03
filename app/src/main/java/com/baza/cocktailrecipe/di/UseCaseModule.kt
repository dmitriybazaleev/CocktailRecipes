package com.baza.cocktailrecipe.di

import android.content.Context
import com.baza.cocktailrecipe.presentation.module.data.api.CocktailApi
import com.baza.cocktailrecipe.presentation.module.data.room.DrinksDao
import com.baza.cocktailrecipe.presentation.module.data.room.IngredientDao
import com.baza.cocktailrecipe.presentation.module.domain.*
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

    @Provides
    fun provideIngredientUseCase(
        api: CocktailApi,
        dao: IngredientDao
    ): SearchByIngredientUseCase = SearchByIngredientUseCase(api, dao)

    @Provides
    fun provideSelectLanguagesUseCase(
        appContext: Context
    ) = SelectLanguageUseCase(appContext)
}