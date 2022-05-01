package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.ui.event.BaseEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Базовый класс для ViewModel
 * Данный класс содержит методы, для работы с базовыми эвентами:
 * 1 - Отправить простой эвент в
 * [com.baza.cocktailrecipe.presentation.module.ui.fragments.BaseFragment]
 * 2 - Возможность излучать любой эвент, котрый есть в
 * [com.baza.cocktailrecipe.presentation.module.ui.event.BaseEvent]
 */
abstract class BaseViewModel : ViewModel() {

    private val _baseEvent = MutableSharedFlow<BaseEvent>()

    /**
     * Метод проверяет возможные ошибки, которые могли произойти при запросе.
     * При определенной ошибки метод отправляет реактивный эвент, который подписался
     * [com.baza.cocktailrecipe.presentation.module.ui.fragments.BaseFragment]
     * @param t - Ошибка, который выкинул OkHttp
     */
    suspend fun showErrorDialogByException(t: Throwable) {
        when (t) {
            is HttpException -> {
                emitBaseEvent(
                    BaseEvent.ActionDialogEventRes(
                        title = R.string.error,
                        message = R.string.network_error,
                        negativeButtonText = R.string.cancel,
                    )
                )
            }
            is UnknownHostException -> {
                emitBaseEvent(
                    BaseEvent.ActionDialogEventRes(
                        title = R.string.error,
                        message = R.string.check_internet_connection,
                        negativeButtonText = R.string.cancel,
                    )
                )
            }
            is SocketTimeoutException -> {
                emitBaseEvent(
                    BaseEvent.ActionDialogEventRes(
                        title = R.string.error,
                        message = R.string.something_went_wrong,
                        negativeButtonText = R.string.cancel,
                    )
                )
            }
        }
    }


    /**
     * Данный метод может отправлять реактивный event в
     * [com.baza.cocktailrecipe.presentation.module.ui.fragments.BaseFragment]
     * @param baseEvent - Параметр базового эвента
     */
    suspend fun emitBaseEvent(baseEvent: BaseEvent) {
        _baseEvent.emit(baseEvent)
    }

    val baseEvent: SharedFlow<BaseEvent>
        get() = _baseEvent
}