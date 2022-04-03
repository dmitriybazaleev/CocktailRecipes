package com.baza.cocktailrecipe.presentation.module.data

import android.content.Context
import android.content.SharedPreferences
import com.baza.cocktailrecipe.presentation.base.App
import java.util.*

object PreferencesCache {

    private const val PREFERENCES_NAME = "DrinksCache"
    private const val LANGUAGE_TYPE_KEY = "languageType"

    private fun getPreferences(): SharedPreferences = App.app.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    private fun getPreferencesEditor(): SharedPreferences.Editor = getPreferences().edit()

    @set:Synchronized
    var language: String?
        set(value) = getPreferencesEditor().putString(LANGUAGE_TYPE_KEY, value).apply()
        get() = getPreferences().getString(LANGUAGE_TYPE_KEY, Locale.getDefault().language)

}