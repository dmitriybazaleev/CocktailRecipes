package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.presentation.base.App
import com.baza.cocktailrecipe.presentation.module.data.api.DRINKS
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.domain.SearchByNameUseCase
import com.baza.cocktailrecipe.presentation.module.ui.event.BaseEvent
import com.baza.cocktailrecipe.presentation.module.ui.event.SearchEvent
import com.baza.cocktailrecipe.presentation.module.ui.fromJsonArray
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toDrinkEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toSearchViewType
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkUiEntitySearch
import com.baza.cocktailrecipe.presentation.module.ui.state.SearchByNameState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class SearchByNameViewModel : BaseViewModel() {

    private val _searchByNameLiveData = MutableLiveData<SearchByNameState>()

    /**
     * Хранит текущее состояние о Фрагменте
     */
    private val mState = SearchByNameState()

    private val _mSearchEvent = MutableSharedFlow<SearchEvent>()

    // Данный ArrayList хранит данные коктейлей из Room
    private val mSavedCocktailList = mutableListOf<DrinkEntity>()

    val searchEvent: SharedFlow<SearchEvent>
        get() = _mSearchEvent.asSharedFlow()

    @Inject
    lateinit var searchByNameUseCase: SearchByNameUseCase

    init {
        App.appComponent?.inject(this)
        getSavedCocktailsDatabase()
    }

    companion object {
        const val TAG = "searchByName"
    }

    /**
     * Данный метод ищет все подходящее коктейли
     */
    fun onStartSearch(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setProgressState(true)
            setPlaceholderState(false)
            updateUiAsync()
            searchByNameUseCase.onSearchCocktail(
                name = query,
                onSuccess = { response ->
                    if (!response.get(DRINKS).isJsonNull) {
                        Log.d(TAG, "response entity: $response")

                        val listResult = response.fromJsonArray<DrinkEntity>(DRINKS)
                        Log.d(TAG, "list result size: ${listResult?.size}")
                        listResult?.let { searchResult ->
                            Log.d(TAG, "from json is success!")
                            setCurrentListState(
                                searchResult,
                                R.string.search_result,
                                false
                            )
                        } ?: run {
                            Log.d(TAG, "from json is unsuccessful")
                        }

                    } else {
                        Log.d(TAG, "nothing has been found")
                        resetCurrentList()
                        setPlaceholderState(true)
                    }

                    setProgressState(false)
                    updateUiAsync()

                },
                onError = { e ->
                    setProgressState(false)
                    updateUiAsync()

                    withContext(Dispatchers.Main) {
                        showErrorDialogByException(e)
                    }
                }
            )
        }
    }

    /**
     * Данный метод возвращает список сохраненных коктейлей из Room
     */
    private fun getSavedCocktailsDatabase() {
        onClearState()
        viewModelScope.launch(Dispatchers.IO) {
            searchByNameUseCase.getSavedDrinks(
                onSuccess = { response ->
                    Log.d(TAG, "saved drinks: $response")
                    if (response.isNotEmpty()) {
                        mSavedCocktailList.addAll(response)

                        setCurrentListState(
                            mSavedCocktailList,
                            R.string.search_history,
                            true
                        )

                        withContext(Dispatchers.Main) {
                            updateUi()
                        }

                    } else {
                        Log.d(TAG, "Nothing has been found inside db!")
                    }
                },
                onError = { e ->
                    e.printStackTrace()
                }
            )
        }
    }

    /**
     * Данный метод сетит в State сохраненный список из Room
     */
    fun getSavedCocktails() {
        setCurrentListState(
            mSavedCocktailList,
            R.string.search_history,
            true
        )

        updateUi()
    }

    /**
     * Данный метод обновляет текущее состояние списка
     * @param newList - Данные коктейлей из Room / результата поиска
     * @param labelRes - label res, который будет добавлять ViewType header
     * @param includeSwipe - Будет ли активен ItemTouchHelper в списке
     */
    private fun setCurrentListState(
        newList: List<DrinkEntity>,
        @StringRes labelRes: Int,
        includeSwipe: Boolean
    ) {
        mState.searchResult = newList.toSearchViewType(labelRes, includeSwipe)
    }

    /**
     * Устанавливает состояние ProgressBar
     */
    private fun setProgressState(isShowProgress: Boolean) {
        if (isShowProgress != mState.isShowProgress)
            mState.isShowProgress = isShowProgress
    }

    /**
     * Устанавливает состояние Placeholder
     */
    private fun setPlaceholderState(isShowPlaceholder: Boolean) {
        if (isShowPlaceholder != mState.isShowPlaceholder)
            mState.isShowPlaceholder = isShowPlaceholder
    }

    /**
     * Данный метод сбрасывает состояние Ui части
     */
    private fun onClearState() {
        resetCurrentList()
        setPlaceholderState(false)
        setProgressState(false)
        updateUi()
    }

    /**
     * Метод добавляет в Room выбранный коктейль
     */
    fun onInsertClickedCocktails(clickedItem: DrinkUiEntitySearch) {
        viewModelScope.launch(Dispatchers.IO) {
            val drinkEntity = clickedItem.toDrinkEntity()
            searchByNameUseCase.onInsertDrink(
                drinkEntity,
                onSuccess = {
                    val result = mSavedCocktailList.find {
                        it == drinkEntity
                    }
                    if (result == null) mSavedCocktailList.add(drinkEntity)

                    withContext(Dispatchers.Main) {
                        emitEvent(
                            SearchEvent.ShowCocktailEvent(
                                drinkEntity
                            )
                        )
                    }
                },
                onError = { e ->
                    withContext(Dispatchers.Main) {
                        emitBaseEvent(
                            BaseEvent.ActionDialogEventRes(
                                title = R.string.error,
                                message = R.string.something_went_wrong,
                                negativeButtonText = R.string.ok
                            )
                        )
                    }
                    e.printStackTrace()
                }
            )
        }
    }

    /**
     * Сбрасывает текущее состояние списка
     */
    private fun resetCurrentList() {
        mState.searchResult = mutableListOf()
    }

    /**
     * Данный метод удаляет item по имени коктейля
     * Вызывается тогда, когда происходит свайп
     */
    fun onRemoveItem(item: DrinkUiEntitySearch, position: Int) {
        Log.d(TAG, "swiped position: $position Model swiped: $item")
        viewModelScope.launch(Dispatchers.IO) {
            item.idDrink.let { drink ->
                searchByNameUseCase.onRemoveDrink(
                    drink,
                    onSuccess = {
                        try {
                            mSavedCocktailList.remove(item.toDrinkEntity())

                            /**
                             * Если текущий список показывает только label, то очищаем весь список
                             */
                            if (mState.searchResult.size == 1) {
                                resetCurrentList()

                            } else {
                                setCurrentListState(
                                    mSavedCocktailList,
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
                        withContext(Dispatchers.Main) {
                            emitBaseEvent(
                                BaseEvent.ActionDialogEventRes(
                                    title = R.string.error,
                                    message = R.string.something_went_wrong,
                                    negativeButtonText = R.string.ok
                                )
                            )
                        }
                        e.printStackTrace()
                    }
                )
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

    private suspend fun emitEvent(event: SearchEvent) {
        _mSearchEvent.emit(event)
    }

    val searchByNameLiveData: LiveData<SearchByNameState>
        get() = _searchByNameLiveData
}