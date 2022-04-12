package com.baza.cocktailrecipe.presentation.module.ui.event

import android.view.View
import androidx.annotation.StringRes
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity

/**
 * Данный sealed class будет служить для обработки одноразовых эвентов
 * Идея реализации простенького шаблона MVI
 */
sealed class SearchEvent {

    /**
     * Данный эвент показывает полную информацию о диалоге
     * @param selectedEntity - кликнутый item
     */
    data class ShowCocktailEvent(
        val selectedEntity: DrinkEntity
    ) : SearchEvent()

    /**
     * Данный эвент служит в случае:
     * Если список показывает данные из Room и просходит свайп элемента.
     * И если вернет ошибку, то элемент будет восстановлен
     */
    data class RestorePositionEvent(
        val position: Int,
        val entity: DrinkEntity
    ) : SearchEvent()
}
