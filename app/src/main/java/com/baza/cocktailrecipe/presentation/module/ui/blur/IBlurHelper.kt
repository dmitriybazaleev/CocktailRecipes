package com.baza.cocktailrecipe.presentation.module.ui.blur

import android.graphics.Bitmap

/**
 * Интерфейс, который реализован в классе
 * [com.baza.cocktailrecipe.presentation.module.ui.blur.BlurHelperImpl]
 * Данный интерефейс методы, которые работают с blur эффектом
 */
interface IBlurHelper {

    /**
     * Данный метод начинает blur картинку по url. Алгоритм такой:
     * 1 - Грузится url в Glide
     * 2 - Блюрит картинку и возвращает bitmap
     */
    suspend fun blurByUrl(url: String?, onSuccess: (bitmap: Bitmap?) -> Unit)
}