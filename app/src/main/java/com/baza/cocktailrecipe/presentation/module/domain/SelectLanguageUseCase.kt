package com.baza.cocktailrecipe.presentation.module.domain

import android.content.Context
import com.baza.cocktailrecipe.presentation.module.ui.getLanguagesStr
import javax.inject.Inject

class SelectLanguageUseCase @Inject constructor(
    private val appContext: Context
) {

    suspend fun getLanguagesStr(
        onSuccess: suspend (strJson: String) -> Unit,
        onError: suspend (e: Exception) -> Unit
    ) {
        try {
            onSuccess.invoke(appContext.getLanguagesStr())

        } catch (e: Exception) {
            onError.invoke(e)
        }
    }
}