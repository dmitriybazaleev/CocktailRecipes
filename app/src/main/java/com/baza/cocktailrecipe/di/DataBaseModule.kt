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
        room: RoomCreator
    ): DrinksDao = room.getDrinkDao()

    @Provides
    fun provideIngredientDao(
        room: RoomCreator
    ) : IngredientDao = room.getIngredientsDao()


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