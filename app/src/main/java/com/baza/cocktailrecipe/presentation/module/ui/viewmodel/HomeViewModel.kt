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
import com.baza.cocktailrecipe.presentation.module.ui.getGeneratedLetter
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toRecommendationEntity
import com.baza.cocktailrecipe.presentation.module.ui.state.HomeState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

// TODO: 12.04.2022 Переделать логику!!
class HomeViewModel : BaseViewModel() {

    private val _homeLiveData = MutableLiveData<HomeState>()
    private val mHomeState = HomeState()

    private val _mHomeEvent = MutableSharedFlow<HomeEvent>()

    @Inject
    lateinit var homeUseCase: HomeUseCase

    init {
        App.appComponent?.inject(this)
        getCocktails(false)
    }


    fun getCocktails(isRefresh: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isRefresh) {
                setSwipeState(true)

            } else {
                setProgressState(true)
            }
            updateUiAsync()

            try {
                val randomList = homeUseCase.getRandomCocktail()
                val byLetterList =
                    homeUseCase.getCocktailsByLetter(getGeneratedLetter())

                if (!randomList.get(DRINKS).isJsonNull && !byLetterList.get(DRINKS).isJsonNull) {
                    val randomCocktails = Gson().fromJson<List<DrinkEntity>>(
                        byLetterList.getAsJsonArray(DRINKS),
                        object : TypeToken<List<DrinkEntity>>() {}.type
                    )

                    /**
                     * Т.к сервер отправляет рандомный коктейль массивом
                     * Берем 0 индекс
                     */
                    val randomCocktailList = Gson().fromJson<DrinkEntity>(
                        randomList.getAsJsonArray(DRINKS).get(0),
                        object : TypeToken<DrinkEntity>() {}.type
                    )
                    Log.d(TAG, "Current recommendation list: ${mHomeState.recommendationsList}")
                    mHomeState.randomList = randomCocktails
                    mHomeState.recommendationsList = randomCocktails.toRecommendationEntity()
                    mHomeState.randomDrink = randomCocktailList

                } else {
                    Log.d(TAG, "response is unsuccessful")
//                    resetCurrentData()
                }

            } catch (e: Exception) {
                e.printStackTrace()

                withContext(Dispatchers.Main) {

                }

            } finally {
                setProgressState(false)
                setSwipeState(false)
                updateUiAsync()
            }
        }
    }

    private fun resetCurrentData() {
        mHomeState.randomDrink = null
        mHomeState.randomList = mutableListOf()
        mHomeState.recommendationsList = mutableListOf()
    }

    private suspend fun emitEvent(homeEvent: HomeEvent) {
        _mHomeEvent.emit(homeEvent)
    }

    private fun setProgressState(isShow: Boolean) {
        if (mHomeState.isShowProgress != isShow)
            mHomeState.isShowProgress = isShow
    }

    private fun setSwipeState(isSwiped: Boolean) {
        if (mHomeState.isRefreshing != isSwiped)
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