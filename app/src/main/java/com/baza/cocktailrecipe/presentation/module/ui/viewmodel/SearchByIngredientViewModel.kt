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
import com.baza.cocktailrecipe.presentation.module.ui.state.SearchByIngredientState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchByIngredientViewModel : ViewModel() {

    @Inject
    lateinit var searchByIngredientUseCase: SearchByIngredientUseCase

    private val _searchLiveData = MutableLiveData<SearchByIngredientState>()
    private val mIngredientsState = SearchByIngredientState()

    init {
        App.appComponent?.inject(this)
    }

    companion object {
        const val TAG = "searchByIngredient"
    }

    fun onSearch(ingredientQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateProgressState(true)
            updatePlaceholderState(false)
            updateUiAsync()
            searchByIngredientUseCase.onSearchIngredient(
                ingredientQuery,
                onSuccess = { response ->
                    if (!response.get(INGREDIENTS).isJsonNull) {
                        val type = object : TypeToken<List<IngredientEntity>>() {}.type
                        val listResult: List<IngredientEntity> =
                            Gson().fromJson(response.getAsJsonArray(INGREDIENTS), type)
                        Log.d(TAG, "ingredient list: $listResult")
                        onHandleResponse(listResult)

                    } else {
                        Log.d(TAG, "Nothing has been found!")
                        resetList()
                        updatePlaceholderState(true)
                        updateUiAsync()
                    }
                },
                onError = { e ->
                }
            )
        }
    }

    fun onClearResponse() {
        resetList()
        updateProgressState(false)
        updatePlaceholderState(false)
        updateUi()
    }

    private fun updateProgressState(isShowProgress: Boolean) {
        if (mIngredientsState.isShowProgress != isShowProgress)
            mIngredientsState.isShowProgress = isShowProgress
    }

    private fun updatePlaceholderState(isShow: Boolean) {
        if (mIngredientsState.isShowPlaceholder != isShow)
            mIngredientsState.isShowPlaceholder = isShow
    }

    private fun resetList() {
        mIngredientsState.searchResult = listOf()
    }

    private fun onHandleResponse(listResult: List<IngredientEntity>) {
        updateProgressState(false)
    }

    private fun updateUi() {
        _searchLiveData.value = mIngredientsState
    }

    private fun updateUiAsync() = _searchLiveData.postValue(mIngredientsState)

    val searchLiveData: LiveData<SearchByIngredientState>
        get() = _searchLiveData
}