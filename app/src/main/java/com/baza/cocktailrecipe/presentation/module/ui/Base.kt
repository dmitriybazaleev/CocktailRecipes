package com.baza.cocktailrecipe.presentation.module.ui

import android.app.Activity
import android.content.Context
import com.baza.cocktailrecipe.R
import java.io.*
import java.util.*

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