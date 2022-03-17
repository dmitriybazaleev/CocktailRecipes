package com.baza.cocktailrecipe.presentation.module.ui

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.lang.Exception

@CheckResult
@ExperimentalCoroutinesApi
fun EditText.textChanges(): Flow<CharSequence?> = callbackFlow {
    val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            trySend(s)
        }
    }
    addTextChangedListener(listener)
    awaitClose { removeTextChangedListener(listener) }
}.onStart {
    emit(text) }

fun Fragment.doDelay(time: Long, action: () -> Unit) {
    this.lifecycleScope.launch {
        try {
            delay(time)

            action.invoke()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun FragmentActivity.doDelay(time: Long, action: () -> Unit) {
    this.lifecycleScope.launch {
        try {
            delay(time)

            action.invoke()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
