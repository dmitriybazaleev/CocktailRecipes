package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.presentation.base.App
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.domain.HomeUseCase
import com.baza.cocktailrecipe.presentation.module.ui.event.HomeEvent
import com.baza.cocktailrecipe.presentation.module.ui.fragments.TAG
import com.baza.cocktailrecipe.presentation.module.ui.state.HomeState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class HomeViewModel : BaseViewModel() {

    private val _homeLiveData = MutableLiveData<HomeState>()
    private val mHomeState = HomeState()

    private val _mHomeEvent = MutableSharedFlow<HomeEvent>()

    @Inject
    lateinit var homeUseCase: HomeUseCase

    init {
        App.appComponent?.inject(this)
        getRandomCocktail()
    }

    private fun getRandomCocktail() {
        viewModelScope.launch(Dispatchers.IO) {
            homeUseCase.getRandomCocktail(
                success = { entity ->
                    val type = object : TypeToken<List<DrinkEntity>>() {}.type
                    val requestsList = Gson()
                        .fromJson<List<DrinkEntity>>(
                            entity.getAsJsonArray("drinks"),
                            type
                        )
                    mHomeState.randomCocktail = requestsList
                    Log.d(TAG, "random result: $requestsList")
                    updateUiAsync()

                }, error = { e ->
                    viewModelScope.launch(Dispatchers.Main) {
                        onHandleError(e)
                    }
                }
            )
        }
    }

    private suspend fun onHandleError(e: Exception) {
        when (e) {
            is HttpException -> {

            }
            is UnknownHostException -> {
                _mHomeEvent.emit(HomeEvent.NetworkError("Ошибка", "Проверьте подключение!"))
            }
            is SocketTimeoutException -> {
                _mHomeEvent.emit(HomeEvent.NetworkError("Ошибка", "Попробуйте попытку позже"))
            }
        }
    }

    fun setSwipeState(isSwiped: Boolean) {
        mHomeState.isRefreshing = isSwiped
        updateUi()
    }

    private fun updateUi() {
        _homeLiveData.value = mHomeState
    }

    private fun updateUiAsync() {
        _homeLiveData.postValue(mHomeState)
    }
    

    val homeEvent: SharedFlow<HomeEvent>
        get() = _mHomeEvent.asSharedFlow()

    val homeLiveData: LiveData<HomeState>
        get() = _homeLiveData
}