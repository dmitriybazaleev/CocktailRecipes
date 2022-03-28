package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.presentation.base.App
import com.baza.cocktailrecipe.presentation.module.data.api.INGREDIENTS
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity
import com.baza.cocktailrecipe.presentation.module.domain.SearchByIngredientUseCase
import com.baza.cocktailrecipe.presentation.module.ui.event.ErrorEvent
import com.baza.cocktailrecipe.presentation.module.ui.event.SearchIngredientEvent
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toSearchType
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkUiEntitySearch
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.SearchNameUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.state.SearchIngredientState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class SearchByIngredientViewModel : BaseViewModel() {

    private val _searchIngredientLiveData = MutableLiveData<SearchIngredientState>()
    private val mSearchState = SearchIngredientState()

    private val _mIngredientEvent = MutableSharedFlow<SearchIngredientEvent>()

    val searchIngredientLiveData: LiveData<SearchIngredientState>
        get() = _searchIngredientLiveData

    @Inject
    lateinit var ingredientUseCase: SearchByIngredientUseCase

    init {
        App.appComponent?.inject(this)
        getSearchHistory()
    }

    companion object {
        const val TAG = "searchByIngredient"
    }

    /**
     * Данный метод будет производить поиск по ингредиенту
     * @param searchInput - Ввод текста
     */
    fun onStartSearch(searchInput: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setProgressState(true)
            setPlaceholderState(false)
            updateUiAsync()

            ingredientUseCase.onSearchIngredient(
                searchInput,
                onSuccess = { response ->
                    if (!response.get(INGREDIENTS).isJsonNull) {
                        val listResult = Gson().fromJson<List<IngredientEntity>>(
                            response.getAsJsonArray(INGREDIENTS),
                            object : TypeToken<List<IngredientEntity>>() {}.type
                        )
                        Log.d(TAG, "ingredient list result: $listResult")
                        mSearchState.searchResult = listResult.toSearchType(
                            "Результат поиска",
                            false
                        )

                    } else {
                        Log.d(TAG, "ingredient result is empty!")
                        resetCurrentList()
                        setPlaceholderState(true)
                    }

                    setProgressState(false)
                    updateUiAsync()
                },
                onError = { e ->
                    e.printStackTrace()
                    setProgressState(false)
                    updateUiAsync()

                    withContext(Dispatchers.Main) {
                        onHandleException(e)
                    }
                }
            )
        }
    }

    /**
     * Данный метод будет искать все сохраненные игредиенты в Room
     */
    fun getSearchHistory() {
        onClearCurrentList()
        viewModelScope.launch(Dispatchers.IO) {
            ingredientUseCase.getSearchHistory(
                onSuccess = { savedIngredients ->
                    if (savedIngredients.isNotEmpty()) {
                        Log.d(TAG, "Current saved ingredient: $savedIngredients")
                        mSearchState.searchResult = savedIngredients.toSearchType(
                            "История поиска",
                            true
                        )


                    } else {
                        Log.d(TAG, "saved ingredient is empty!")
                    }

                    updateUiAsync()
                },
                onError = { e ->
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        onHandleException(e)
                    }
                }
            )
        }
    }

    /**
     * Данный метод будет добавлять выбранный ингредиент из списка
     * @param item - Выбранный элемент Entity
     */
    fun insertIngredient(item: IngredientEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "clicked item: $item")
            ingredientUseCase.onInsertIngredient(
                item,
                onSuccess = {
                    Log.d(TAG, "insert data onSuccess!")

                    withContext(Dispatchers.Main) {
                        emitEvent(
                            SearchIngredientEvent.ShowCocktailDetailsEvent(
                                item
                            )
                        )
                    }
                },
                onError = { e ->
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        onHandleException(e)
                    }
                }
            )
        }
    }

    /**
     * Метод удаляет ингредиент из Room, при свайпе
     * @param swipedEntity - Выбранный элемент Entity
     */
    fun onRemoveIngredient(swipedEntity: DrinkUiEntitySearch) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Swiped entity: $swipedEntity")
            ingredientUseCase.onRemoveIngredient(
                swipedEntity.idDrink,
                onSuccess = {
                    Log.d(TAG, "Deleted success!")
                    val newList = mutableListOf<SearchNameUiEntity>()
                    newList.addAll(mSearchState.searchResult)
                    newList.remove(swipedEntity)
                    if (newList.size == 1) {
                        resetCurrentList()

                    } else {
                        mSearchState.searchResult = newList
                    }
                    updateUiAsync()

                },
                onError = { e ->
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {
                        onHandleException(e)
                    }
                }
            )
        }
    }

    private suspend fun onHandleException(e: Exception) {
        when (e) {
            is UnknownHostException -> {

            }
            is HttpException -> {

            }
            is SocketTimeoutException -> {

            }
        }
    }

    private fun onClearCurrentList() {
        resetCurrentList()
        setPlaceholderState(false)
        setProgressState(false)
        updateUi()
    }

    fun resetCurrentList() {
        mSearchState.searchResult = mutableListOf()
    }

    private fun setProgressState(isShowProgress: Boolean) {
        if (isShowProgress != mSearchState.isShowProgress) {
            mSearchState.isShowProgress = isShowProgress
        }
    }

    private fun setPlaceholderState(isShow: Boolean) {
        if (isShow != mSearchState.isShowPlaceholder) {
            mSearchState.isShowPlaceholder = isShow
        }
    }

    private fun updateUi() {
        _searchIngredientLiveData.value = mSearchState
    }

    private suspend fun emitEvent(newEvent: SearchIngredientEvent) {
        _mIngredientEvent.emit(newEvent)
    }

    private fun updateUiAsync() = _searchIngredientLiveData.postValue(mSearchState)


    val ingredientEvent: SharedFlow<SearchIngredientEvent>
        get() = _mIngredientEvent
}