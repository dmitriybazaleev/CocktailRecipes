package com.baza.cocktailrecipe.di

import android.content.Context
import androidx.room.Room
import com.baza.cocktailrecipe.presentation.module.data.room.DrinksDao
import com.baza.cocktailrecipe.presentation.module.data.room.IngredientDao
import com.baza.cocktailrecipe.presentation.module.data.room.RoomCreator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    fun provideDrinksDao(
        creator: RoomCreator
    ): DrinksDao = creator.getDrinkDao()

    @Provides
    fun provideIngredientDao(
        creator: RoomCreator
    ) : IngredientDao = creator.getIngredientsDao()


    @Provides
    @Singleton
    fun provideRoom(appContext: Context): RoomCreator = Room.databaseBuilder(
        appContext,
        RoomCreator::class.java,
        "Cocktails"
    )
        .fallbackToDestructiveMigration()
        .build()
}