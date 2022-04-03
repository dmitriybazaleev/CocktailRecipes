package com.baza.cocktailrecipe.presentation.module.ui

import android.app.Activity
import android.content.Context
import com.baza.cocktailrecipe.R
import java.io.*
import java.util.*


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

fun Activity.setAppLanguage(code: String?) {
    code?.let {
        try {
            val locale = Locale(code)
            Locale.setDefault(locale)
            val res = resources ?: return
            val conf = res.configuration ?: return
            conf.setLocale(locale)

            res.updateConfiguration(conf, res.displayMetrics)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}