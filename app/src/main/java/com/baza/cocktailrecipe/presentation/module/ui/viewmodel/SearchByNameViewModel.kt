package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.base.App
import com.baza.cocktailrecipe.presentation.module.data.api.DRINKS
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.domain.SearchByNameUseCase
import com.baza.cocktailrecipe.presentation.module.ui.event.SearchEvent
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toDrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toViewType
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkUiEntitySearch
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.SearchUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.state.SearchByNameState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class SearchByNameViewModel : ViewModel() {

    private val _searchByNameLiveData = MutableLiveData<SearchByNameState>()

    /**
     * Хранит текущее состояние о Фрагменте
     */
    private val mState = SearchByNameState()

    private val _mSearchEvent = MutableSharedFlow<SearchEvent>()

//    var isCocktailSaved = false

    val searchEvent: SharedFlow<SearchEvent>
        get() = _mSearchEvent.asSharedFlow()

    @Inject
    lateinit var searchByNameUseCase: SearchByNameUseCase

    init {
        App.appComponent?.inject(this)
        getSavedCocktails()
    }

    companion object {
        const val TAG = "searchByName"
    }


    /**
     * Данный метод ищет все подходящее коктели
     */
    fun onStartSearch(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setProgressState(true)
            setPlaceholderState(false)
            updateUiAsync()
            try {
                val json = searchByNameUseCase.onSearchCocktail(query)

                if (!json.get(DRINKS).isJsonNull) {
                    val type = object : TypeToken<List<DrinkEntity>>() {}.type
                    val response: List<DrinkEntity> = Gson().fromJson(
                        json.getAsJsonArray(DRINKS), type
                    )
                    Log.d(TAG, "response entity: $response")
                    onHandleResponse(response)

                } else {
                    Log.d(TAG, "nothing has been found")
                    resetCurrentList()
                    setPlaceholderState(true)
                    updateUiAsync()
                }

            } catch (e: Exception) {
                setProgressState(false)
                updateUiAsync()

                withContext(Dispatchers.Main) {
                    onHandleException(e)
                }
            }
        }
    }

    private suspend fun onHandleException(t: Throwable) {
        when (t) {
            is UnknownHostException -> {
                _mSearchEvent.emit(
                    SearchEvent.DialogEvent(
                        titleRes = R.string.error,
                        messageRes = R.string.check_internet_connection,
                        negativeButtonTextRes = R.string.cancel,
                    )
                )
            }
            is SocketTimeoutException -> {
                _mSearchEvent.emit(
                    SearchEvent.DialogEvent(
                        titleRes = R.string.error,
                        messageRes = R.string.something_went_wrong,
                        negativeButtonTextRes = R.string.cancel,
                    )
                )
            }
            is HttpException -> {
                _mSearchEvent.emit(
                    SearchEvent.DialogEvent(
                        titleRes = R.string.error,
                        messageRes = R.string.network_error,
                        negativeButtonTextRes = R.string.cancel,
                    )
                )
            }
        }
    }

    fun getSavedCocktails() {
        onClearCurrentList()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cacheDrinks = searchByNameUseCase.getSavedDrinks()
                Log.d(TAG, "saved drinks: $cacheDrinks")
                if (cacheDrinks.isNotEmpty()) {
                    mState.searchResult = cacheDrinks.toViewType("История поиска", true)

                    withContext(Dispatchers.Main) {
                        updateUi()
                    }

                } else {
                    Log.d(TAG, "Nothing has been found inside db!")
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun onHandleResponse(newResult: List<DrinkEntity>) {
        mState.searchResult = newResult.toViewType("Результат поиска", false)
        setProgressState(false)
        updateUiAsync()
    }

    private fun setProgressState(isShowProgress: Boolean) {
        if (isShowProgress != mState.isShowProgress)
            mState.isShowProgress = isShowProgress
    }

    private fun setPlaceholderState(isShowPlaceholder: Boolean) {
        if (isShowPlaceholder != mState.isShowPlaceholder)
            mState.isShowPlaceholder = isShowPlaceholder
    }

    private fun onClearCurrentList() {
        resetCurrentList()
        setPlaceholderState(false)
        setProgressState(false)
        updateUi()
    }


    fun onInsertClickedCocktails(clickedItem: DrinkUiEntitySearch) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                searchByNameUseCase.onInsertDrink(clickedItem.toDrinkEntity())

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mSearchEvent.emit(
                        SearchEvent.DialogEvent(
                            titleRes = R.string.error,
                            messageRes = R.string.something_went_wrong,
                            negativeButtonTextRes = R.string.ok
                        )
                    )
                }
                e.printStackTrace()
            }
        }
    }

    private fun resetCurrentList() {
        mState.searchResult = mutableListOf()
    }

    /**
     * Данный метод удаляет item по имени коктеля
     * Вызывается тогда, когда происходит свайп
     */
    fun onRemoveItem(item: DrinkUiEntitySearch, position: Int) {
        Log.d(TAG, "swiped position: $position Model swiped: $item")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                item.idDrink?.let { drink ->
                    searchByNameUseCase.onRemoveDrink(drink)
                    val newList = mutableListOf<SearchUiEntity>()
                    newList.addAll(mState.searchResult)
                    newList.remove(item)
                    if (newList.size == 1) {
                        resetCurrentList()

                    } else {
                        mState.searchResult = newList
                    }
                    updateUiAsync()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _mSearchEvent.emit(
                        SearchEvent.DialogEvent(
                            titleRes = R.string.error,
                            messageRes = R.string.something_went_wrong,
                            negativeButtonTextRes = R.string.ok
                        )
                    )
                }
                e.printStackTrace()
            }
        }
    }


    /**
     * Обновляет LiveData в главном потоке
     */
    private fun updateUi() {
        _searchByNameLiveData.value = mState
    }

    /**
     * Обновляет LiveData в другом потоке
     */
    private fun updateUiAsync() {
        _searchByNameLiveData.postValue(mState)
    }

    val searchByNameLiveData: LiveData<SearchByNameState>
        get() = _searchByNameLiveData
}