package com.baza.cocktailrecipe.presentation.module.ui

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

inline fun <reified T> JsonObject.fromJsonArray(header: String): List<T>? {
    return try {
        Gson().fromJson<List<T>>(
            this.getAsJsonArray(header),
            object : TypeToken<List<T>>() {}.type
        )
    } catch (e: Exception) {
        null
    }
}


inline fun <reified T> JsonObject.fromJsonObject(header: String): T? {
    return try {
        Gson().fromJson<T>(
            this.getAsJsonObject(header),
            object : TypeToken<T>() {}.type
        )

    } catch (e: Exception) {
        null
    }
}