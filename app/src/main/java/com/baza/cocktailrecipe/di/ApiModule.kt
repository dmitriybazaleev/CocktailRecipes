package com.baza.cocktailrecipe.di

import com.baza.cocktailrecipe.presentation.module.data.api.CocktailApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideCocktailApi(
        retrofitCreator: Retrofit
    ): CocktailApi = retrofitCreator.create(CocktailApi::class.java)
}