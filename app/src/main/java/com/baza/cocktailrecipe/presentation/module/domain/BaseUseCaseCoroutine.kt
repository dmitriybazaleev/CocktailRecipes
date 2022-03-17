package com.baza.cocktailrecipe.presentation.module.domain

import java.lang.Exception

interface BaseUseCaseCoroutine<T> {

    suspend fun execute(success: (T) -> Unit, fail: (Exception) -> Unit)
}
