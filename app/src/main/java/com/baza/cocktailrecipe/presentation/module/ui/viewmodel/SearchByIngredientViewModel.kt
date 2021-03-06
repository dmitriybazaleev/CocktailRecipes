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
import com.baza.cocktailrecipe.presentation.module.ui.event.BaseEvent
import com.baza.cocktailrecipe.presentation.module.ui.event.SearchIngredientEvent
import com.baza.cocktailrecipe.presentation.module.ui.fromJsonArray
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toIngredientEntity
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toSearchType
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.DrinkUiEntitySearch
import com.baza.cocktailrecipe.presentation.module.ui.state.SearchIngredientState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
     * ???????????? ?????????? ?????????? ?????????????????????? ?????????? ???? ??????????????????????
     * @param searchInput - ???????? ????????????
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
                        Log.d(TAG, "ingredients response: $response")
                        val listResult = response.fromJsonArray<IngredientEntity>(INGREDIENTS)
                        Log.d(TAG, "list result size: ${listResult?.size}")
                        listResult?.let {
                            Log.d(TAG, "from json is success!")
                            setListIngredientState(
                                it,
                                R.string.search_result,
                                false
                            )
                        } ?: run {
                            Log.d(TAG, "from json is unsuccessful!")
                        }

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
                        showErrorDialogByException(e)
                    }
                }
            )
        }
    }

    /**
     * ???????????? ?????????? ?????????????????? ?????????????? ?????????????????? ????????????
     * @param newList - ???????????? ?????????????????? ???? Room / ???????????????????? ????????????
     * @param labelRes - label res, ?????????????? ?????????? ?????????????????? ViewType header
     * @param includeSwipe - ?????????? ???? ?????????????? ItemTouchHelper ?? ????????????
     */
    private fun setListIngredientState(
        newList: List<IngredientEntity>,
        @StringRes labelRes: Int,
        includeSwipe: Boolean
    ) {
        mSearchState.searchResult = newList.toSearchType(labelRes, includeSwipe)
    }

    /**
     * ???????????? ?????????? ?????????????????????????? ?? ?????????????? ???????????? ???????????? ???? Room.
     * ???????????? ??????, ?????????? ???????? ???????????????? ???????? ????????????, ???? ?? state ?????????? ???????????? ?????????????????????? ????????????
     * ???? ????????????????????????
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
     * ???????????? ?????????? ?????????? ???????????? ?????? ?????????????????????? ???????????????????? ?? Room
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
                }
            )
        }
    }

    /**
     * ???????????? ?????????? ?????????? ?????????????????? ?????????????????? ???????????????????? ???? ????????????
     * @param item - ?????????????????? ?????????????? Entity
     */
    fun insertIngredient(item: IngredientEntity) {
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
                }
            )
        }
    }

    /**
     * ?????????? ?????????????? ???????????????????? ???? Room, ?????? ????????????
     * @param swipedEntity - ?????????????????? ?????????????? Entity
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
                         * ???????? ?????????????? ???????????? ???????????????????? ???????????? label, ???? ?????????????? ???????? ????????????
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
                        emitBaseEvent(
                            BaseEvent.ActionDialogEventRes(
                                title = R.string.error,
                                message = R.string.something_went_wrong,
                                negativeButtonText = R.string.ok
                            )
                        )
                    }
                }
            )
        }
    }

    /**
     * ?????????? ?????????????? ?????????????? ??????????????????
     */
    private fun onClearState() {
        resetCurrentList()
        setPlaceholderState(false)
        setProgressState(false)
        updateUi()
    }


    /**
     * ???????????????????? ?????????????? ?????????????????? ArrayList
     */
    private fun resetCurrentList() {
        mSearchState.searchResult = mutableListOf()
    }

    /**
     * ???????????? ?????????? ?????????????????????????? ?????????????????? ProgressBar
     */
    private fun setProgressState(isShowProgress: Boolean) {
        if (isShowProgress != mSearchState.isShowProgress) {
            mSearchState.isShowProgress = isShowProgress
        }
    }

    /**
     * ???????????? ?????????? ?????????????????????????? ?????????????????? Placeholder
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