package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.presentation.base.App
import com.baza.cocktailrecipe.presentation.module.data.api.DRINKS
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.domain.SearchByNameUseCase
import com.baza.cocktailrecipe.presentation.module.ui.event.SearchEvent
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.mapSearchEntity
import com.baza.cocktailrecipe.presentation.module.ui.state.SearchByNameState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

// TODO: 15.03.2022 Придумать логику, при ошибки!
class SearchByNameViewModel : ViewModel() {

    private val _searchByNameLiveData = MutableLiveData<SearchByNameState>()
    private val mState = SearchByNameState()

    private val _mSearchEvent = MutableSharedFlow<SearchEvent>()

    val searchEvent: SharedFlow<SearchEvent>
        get() = _mSearchEvent.asSharedFlow()

    @Inject
    lateinit var searchByNameUseCase: SearchByNameUseCase

    init {
        App.appComponent?.inject(this)
    }

    companion object {
        const val TAG = "searchByName"
    }


    fun onStartSearch(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setProgressState(true)
            setPlaceholderState(false)
            updateUiAsync()
            searchByNameUseCase.onSearchCocktail(
                name = query,
                onSuccess = { json ->
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
                },
                onError = { e ->
                    setProgressState(false)
                    updateUi()
                    onHandleException(e)
                })
        }
    }

    private fun onHandleException(t: Throwable) {
        when (t) {
            is UnknownHostException -> {

            }
            is SocketTimeoutException -> {

            }
            is HttpException -> {

            }
        }
    }

    private fun onHandleResponse(newResult: List<DrinkEntity>) {
        val listResult = newResult.mapSearchEntity()
        mState.searchResult = listResult
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

    fun onClearCurrentList() {
        resetCurrentList()
        setPlaceholderState(false)
        setProgressState(false)
        updateUi()
    }

    private fun resetCurrentList() {
        mState.searchResult = listOf()
    }

    private fun updateUi() {
        _searchByNameLiveData.value = mState
    }

    private fun updateUiAsync() {
        _searchByNameLiveData.postValue(mState)
    }

    val searchByNameLiveData: LiveData<SearchByNameState>
        get() = _searchByNameLiveData
}