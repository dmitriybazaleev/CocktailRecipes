package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.base.App
import com.baza.cocktailrecipe.presentation.module.data.api.INGREDIENTS
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity
import com.baza.cocktailrecipe.presentation.module.domain.SearchByIngredientUseCase
import com.baza.cocktailrecipe.presentation.module.ui.event.SearchIngredientEvent
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toIngredientEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toSearchType
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkUiEntitySearch
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

    private val mSavedIngredients = mutableListOf<IngredientEntity>()

    init {
        App.appComponent?.inject(this)
        getSearchHistoryDatabase()
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
                        setListIngredientState(
                            listResult,
                            R.string.search_result,
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
     * Данный метод обновляет текущее состояние списка
     * @param newList - Данные коктейлей из Room / результата поиска
     * @param labelRes - label res, который будет добавлять ViewType header
     * @param includeSwipe - Будет ли активен ItemTouchHelper в списке
     */
    private fun setListIngredientState(
        newList: List<IngredientEntity>,
        @StringRes labelRes: Int,
        includeSwipe: Boolean
    ) {
        mSearchState.searchResult = newList.toSearchType(labelRes, includeSwipe)
    }

    /**
     * Данный метод устанавливает в текущий список данные из Room.
     * Всякий раз, когда юзер закончит ввод текста, то в state будет сетить сохраненный список
     * из ингредиентов
     */
    fun getSavedIngredients() {
        setListIngredientState(
            mSavedIngredients,
            R.string.search_history,
            true
        )
        updateUi()
    }

    /**
     * Данный метод будет искать все сохраненные игредиенты в Room
     */
    private fun getSearchHistoryDatabase() {
        onClearState()
        viewModelScope.launch(Dispatchers.IO) {
            ingredientUseCase.getSearchHistory(
                onSuccess = { savedIngredients ->
                    if (savedIngredients.isNotEmpty()) {
                        Log.d(TAG, "Current saved ingredient: $savedIngredients")
                        mSavedIngredients.addAll(savedIngredients)

                        setListIngredientState(
                            savedIngredients,
                            R.string.search_history,
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
    fun insertIngredient(item: IngredientEntity, itemPosition: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "clicked item: $item")
            ingredientUseCase.onInsertIngredient(
                item,
                onSuccess = {
                    Log.d(TAG, "insert data onSuccess!")
                    val entity = mSavedIngredients.find {
                        it == item
                    }
                    if (entity == null) mSavedIngredients.add(item)

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
                    try {
                        mSavedIngredients.remove(swipedEntity.toIngredientEntity())

                        /**
                         * Если текущий список показывает только label, то очищаем весь список
                         */
                        if (mSearchState.searchResult.size == 1) {
                            resetCurrentList()

                        } else {
                            setListIngredientState(
                                mSavedIngredients,
                                R.string.search_history,
                                true
                            )
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
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

    /**
     * Метод очищает имеющее состояние
     */
    private fun onClearState() {
        resetCurrentList()
        setPlaceholderState(false)
        setProgressState(false)
        updateUi()
    }


    /**
     * Сбрасывает текущее состояние ArrayList
     */
    private fun resetCurrentList() {
        mSearchState.searchResult = mutableListOf()
    }

    /**
     * Данный метод устанавливает состояние ProgressBar
     */
    private fun setProgressState(isShowProgress: Boolean) {
        if (isShowProgress != mSearchState.isShowProgress) {
            mSearchState.isShowProgress = isShowProgress
        }
    }

    /**
     * Данный метод устанавливает состояние Placeholder
     */
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