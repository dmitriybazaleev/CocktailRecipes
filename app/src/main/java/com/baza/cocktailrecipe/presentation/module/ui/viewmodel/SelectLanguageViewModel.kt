package com.baza.cocktailrecipe.presentation.module.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baza.cocktailrecipe.presentation.base.App
import com.baza.cocktailrecipe.presentation.module.data.PreferencesCache
import com.baza.cocktailrecipe.presentation.module.domain.SelectLanguageUseCase
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.adapter.toLanguageUiEntity
import com.baza.cocktailrecipe.presentation.module.data.entity.LanguageEntity
import com.baza.cocktailrecipe.presentation.module.ui.event.SelectLanguageEvent
import com.baza.cocktailrecipe.presentation.module.ui.recyclerview.entity.LanguageUiEntity
import com.baza.cocktailrecipe.presentation.module.ui.state.SelectLanguageState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SelectLanguageViewModel : ViewModel() {

    private val _selectLanguagesLiveData = MutableLiveData<SelectLanguageState>()
    private val mSelectLanguagesState = SelectLanguageState()

    private val _selectLanguageEvent = MutableSharedFlow<SelectLanguageEvent>()

    private val mCurrentLanguage = PreferencesCache.language
    var mSelectedCode: String = ""

    private val mCurrentLanguagesList = mutableListOf<LanguageUiEntity>()

    private var currentSelectedPosition = -1

    companion object {
        const val TAG = "selectLanguage"
    }

    @Inject
    lateinit var selectLanguagesUseCase: SelectLanguageUseCase

    init {
        App.appComponent?.inject(this)
        getAvailableLanguages()
    }

    private fun getAvailableLanguages() {
        viewModelScope.launch(Dispatchers.IO) {
            selectLanguagesUseCase.getLanguagesStr(
                onSuccess = { strJson ->
                    val listLanguages: List<LanguageEntity> = Gson().fromJson(
                        strJson,
                        object : TypeToken<List<LanguageEntity>>() {}.type
                    )
                    Log.d(TAG, "Available languages: $listLanguages")

                    listLanguages.forEachIndexed { index, entity ->
                        val isCurrentCode = entity.code == mCurrentLanguage
                        if (isCurrentCode) {
                            currentSelectedPosition = index
                        }
                        mCurrentLanguagesList.add(
                            entity.toLanguageUiEntity(
                                isCurrentCode
                            )
                        )
                        updateStateList()
                    }
                    updateUiAsync()
                },
                onError = { e ->
                    e.printStackTrace()

                    withContext(Dispatchers.Main) {

                    }
                }
            )
        }
    }

    private fun updateStateList() {
        mSelectLanguagesState.languagesList = mCurrentLanguagesList
    }


    private fun setButtonState(isEnabled: Boolean) {
        mSelectLanguagesState.isButtonEnabled = isEnabled
    }

    private suspend fun emitEvent(event: SelectLanguageEvent) {
        _selectLanguageEvent.emit(event)
    }

    private fun updateUiAsync() = _selectLanguagesLiveData.postValue(mSelectLanguagesState)

    private fun updateUi() {
        _selectLanguagesLiveData.value = mSelectLanguagesState
    }

    fun onCheckChanged(checked: Boolean, position: Int, entity: LanguageUiEntity) {
        Log.d(TAG, "previous position: $currentSelectedPosition current position: $position")
        try {
            if (currentSelectedPosition == position || currentSelectedPosition == -1) return

            setButtonState(mCurrentLanguage != entity.code)
            this.mSelectedCode = entity.code

            mCurrentLanguagesList[currentSelectedPosition].isLanguageSelected = false
            mCurrentLanguagesList[position].isLanguageSelected = checked
            updateStateList()
            updateUi()

            currentSelectedPosition = position


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updatePrefLanguage() {
        PreferencesCache.language = mSelectedCode
    }

    val selectLanguagesLiveData: LiveData<SelectLanguageState>
        get() = _selectLanguagesLiveData

    val selectLanguageEvent: SharedFlow<SelectLanguageEvent>
        get() = _selectLanguageEvent.asSharedFlow()
}