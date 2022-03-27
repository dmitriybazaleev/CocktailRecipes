package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.presentation.base.App
import com.baza.cocktailrecipe.presentation.module.data.api.INGREDIENTS
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity
import com.baza.cocktailrecipe.presentation.module.domain.SearchByIngredientUseCase
import com.baza.cocktailrecipe.presentation.module.ui.event.SearchIngredientEvent
import com.baza.cocktailrecipe.presentation.module.ui.state.SearchIngredientState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchByIngredientViewModel : ViewModel() {

    private val _searchIngredientLiveData = MutableLiveData<SearchIngredientState>()
    private val mSearchState = SearchIngredientState()

    private val _mIngredientEvent = MutableSharedFlow<SearchIngredientEvent>()

    val searchIngredientLiveData: LiveData<SearchIngredientState>
        get() = _searchIngredientLiveData

    @Inject
    lateinit var ingredientUseCase: SearchByIngredientUseCase

    init {
        App.appComponent?.inject(this)
    }

    companion object {
        const val TAG = "searchByIngredient"
    }

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


                    } else {
                        Log.d(TAG, "ingredient result is empty!")
                        setPlaceholderState(true)
                    }

                    setProgressState(false)
                    updateUiAsync()
                },
                onError = { e ->
                    e.printStackTrace()
                    setProgressState(false)
                    updateUiAsync()
                }
            )
        }
    }

    fun getSearchHistory() {
        onClearCurrentList()
        viewModelScope.launch(Dispatchers.IO) {
            ingredientUseCase.getSearchHistory(
                onSuccess = { savedIngredients ->
                    if (savedIngredients.isNotEmpty()) {

                    } else {
                        Log.d(TAG, "saved ingredient is empty!")
                    }
                },
                onError = { e ->
                    e.printStackTrace()
                }
            )
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