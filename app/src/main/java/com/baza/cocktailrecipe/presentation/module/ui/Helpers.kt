package com.baza.cocktailrecipe.presentation.module.ui

import android.content.Context
import com.baza.cocktailrecipe.R
import java.io.*

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

fun getGeneratedLetter(): String = ('a'..'z').random().toString()