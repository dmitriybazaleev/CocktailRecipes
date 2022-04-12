package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.module.ui.event.BaseEvent
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel() {

    private val _baseEvent = MutableSharedFlow<BaseEvent>()

    /**
     * Данный метод отправляет реактивный эвент в Базовый фрагмент
     * Если сервер отдал ошибку и эвент будет показывать
     * [com.baza.cocktailrecipe.presentation.module.ui.dialog.ActionDialog]
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
     * Данный метод излучает эвент в
     * [com.baza.cocktailrecipe.presentation.module.ui.fragments.BaseFragment]
     */
    suspend fun emitBaseEvent(baseEvent: BaseEvent) {
        _baseEvent.emit(baseEvent)
    }

    val baseEvent: SharedFlow<BaseEvent>
        get() = _baseEvent
}