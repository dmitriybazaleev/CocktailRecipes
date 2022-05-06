package com.baza.cocktailrecipe.presentation.module.ui

import android.content.Context
import com.baza.cocktailrecipe.R
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.io.*

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

inline fun <reified T> Gson.fromJsonArrayStr(jsonStr: String) : List<T>? {
    return try {
        this.fromJson<List<T>>(
            jsonStr,
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

fun Context.getLanguagesStr(): String {
    val inputStream: InputStream = resources.openRawResource(R.raw.languages)
    val writer = StringWriter()
    val buffer = CharArray(1024)
    inputStream.use { stream ->
        val reader: Reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        var n: Int
        while (reader.read(buffer).also { n = it } != -1) {
            writer.write(buffer, 0, n)
        }
    }

    return writer.toString()
}