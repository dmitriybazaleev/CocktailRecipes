package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.presentation.base.App
import com.baza.cocktailrecipe.presentation.module.data.api.DRINKS
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.domain.HomeUseCase
import com.baza.cocktailrecipe.presentation.module.ui.event.HomeEvent
import com.baza.cocktailrecipe.presentation.module.ui.fragments.TAG
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toBlurViewType
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toRandomDrink
import com.baza.cocktailrecipe.presentation.module.ui.state.HomeState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
        getCocktails()
    }


    private fun getCocktails() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val randomList = homeUseCase.getRandomCocktail()
                val byLetterList = homeUseCase.getCocktailsByLetter(mGeneratedStr)


                if (!randomList.isJsonNull && !byLetterList.isJsonNull) {
                    val randomCocktailList = Gson().fromJson<List<DrinkEntity>>(
                        randomList.getAsJsonArray(DRINKS),
                        object : TypeToken<List<DrinkEntity>>() {}.type
                    ).toBlurViewType()

                    val cocktailByLetter = Gson().fromJson<List<DrinkEntity>>(
                        byLetterList.getAsJsonArray(DRINKS),
                        object : TypeToken<List<DrinkEntity>>() {}.type
                    ).toRandomDrink()
                    mHomeState.cocktailsList.addAll(randomCocktailList)
                    mHomeState.cocktailsList.addAll(cocktailByLetter)
                    Log.d(
                        TAG,
                        "random list: $randomCocktailList, " +
                                "cocktails list: $cocktailByLetter"
                    )

                    updateUiAsync()

                } else {
                    Log.d(TAG, "")
                }


            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    onHandleError(e)
                }
            }
        }
    }

    private suspend fun onHandleError(e: Exception) {
        when (e) {
            is HttpException -> {
                Log.d(TAG, "http exception! code: ${e.code()}, message: ${e.message}")
            }
            is UnknownHostException -> {
                _mHomeEvent.emit(HomeEvent.NetworkError("Ошибка", "Проверьте подключение!"))
            }
            is SocketTimeoutException -> {
                _mHomeEvent.emit(HomeEvent.NetworkError("Ошибка", "Попробуйте попытку позже"))
            }
        }
    }

    private val mGeneratedStr = ('a'..'z').random().toString()

    private fun setProgressState(isShow: Boolean) {
        mHomeState.isShowProgress = isShow
    }

    private fun setSwipeState(isSwiped: Boolean) {
        mHomeState.isRefreshing = isSwiped
    }

    private fun updateUi() {
        _homeLiveData.value = mHomeState
    }

    private fun updateUiAsync() = _homeLiveData.postValue(mHomeState)

    val homeEvent: SharedFlow<HomeEvent>
        get() = _mHomeEvent.asSharedFlow()

    val homeLiveData: LiveData<HomeState>
        get() = _homeLiveData
}