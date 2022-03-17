package com.baza.cocktailrecipe.presentation.module.data

import android.content.Context
import android.content.SharedPreferences
import com.baza.cocktailrecipe.presentation.base.App

object PreferencesCache {

    const val PREFERENCES_NAME = "DrinksCache"
    const val NIGHT_MODE_KEY = "isNightMode"

    private fun getPreferences(): SharedPreferences = App.app.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    private fun getPreferencesEditor(): SharedPreferences.Editor = getPreferences().edit()

    @set:Synchronized
    var isNightMode: Boolean
        set(value) = getPreferencesEditor().putBoolean(NIGHT_MODE_KEY, value).apply()
        get() = getPreferences().getBoolean(NIGHT_MODE_KEY, false)

}