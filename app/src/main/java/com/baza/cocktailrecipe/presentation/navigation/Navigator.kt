package com.baza.cocktailrecipe.presentation.navigation

import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController

/**
 * Интерфейс для работы с навигацией
 * Реализован в [NavigationController]
 */
interface Navigator {

    /**
     * Данный метод добавляет фрагмент
     * @param destinationId - id фрагмента или destinationId в navigation -> app_graph
     */
    fun addFragment(@IdRes destinationId: Int)

    /**
     * Данный метод добавляет фрагмент
     * @param destinationId - id фрагмента или destinationId в navigation -> app_graph
     * @param args - Аргументы для bundle
     */
    fun addFragment(@IdRes destinationId: Int, vararg args: NavArguments)

    /**
     * Данный метод возвращает к предыдущему фрагменту
     */
    fun popBackStack()

    /**
     * Возвращает NavController
     */
    fun getController() : NavController?

    /**
     * Добавляет фрагмент, а так же можно передать анимацию
     */
    fun addFragment(
        @IdRes destinationId: Int,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int,
        @AnimRes popEnterAnim: Int,
        @AnimRes popExitAnim: Int
    )

    /**
     * Добавляет фрагмент, возможно передать анимацию, а так же аргументы
     */
    fun addFragment(
        @IdRes destinationId: Int,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int,
        @AnimRes popEnterAnim: Int,
        @AnimRes popExitAnim: Int,
        vararg args: NavArguments
    )

    /**
     * Данный метод возвращает текущий фрагмент
     */
    fun getCurrentFragment() : Fragment?
}