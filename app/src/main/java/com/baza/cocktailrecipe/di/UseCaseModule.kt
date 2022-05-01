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
        cocktailApi: CocktailApi
    ): HomeUseCase = HomeUseCase(cocktailApi)

    @Provides
    fun provideSearchByNameUseCase(
        cocktailApi: CocktailApi,
        drinksDao: DrinksDao
    ): SearchByNameUseCase = SearchByNameUseCase(cocktailApi, drinksDao)


    @Provides
    fun provideSavedUseCase(
        drinksDao: DrinksDao,
        ingredientDao: IngredientDao
    ): SavedUseCase = SavedUseCase(drinksDao, ingredientDao)

    @Provides
    fun provideIngredientUseCase(
        cocktailApi: CocktailApi,
        ingredientDao: IngredientDao
    ): SearchByIngredientUseCase = SearchByIngredientUseCase(cocktailApi, ingredientDao)

    @Provides
    fun provideSelectLanguagesUseCase(
        app: Context
    ) = SelectLanguageUseCase(app)
}