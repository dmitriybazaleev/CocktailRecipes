package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.baza.cocktailrecipe.presentation.module.ui.MainActivity

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    companion object {
        private const val TAG = "baseFragment"
    }

    private var _binding: B? = null
    protected val binding: B?
        get() = _binding

    private var _act: MainActivity? = null
    protected val act: MainActivity?
        get() = _act

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _act = activity as? MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(inflater, container, false)

        return requireNotNull(_binding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkHasBottomNavVisibility()
    }

    private fun checkHasBottomNavVisibility() {
        _act?.isShowBottomNav(isShowBottomNavigation())
    }


    override fun onDetach() {
        super.onDetach()
        _act = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun openKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    protected fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    protected open fun isShowBottomNavigation() = true

    protected fun showAlert(
        title: String,
        message: String,
        positiveButtonText: String? = null,
        positiveButtonAction: (() -> Unit)? = null,
        negativeButtonText: String? = null,
        negativeButtonAction: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { dialog, which ->
                positiveButtonAction?.invoke()
            }
            .setNegativeButton(negativeButtonText) { dialog, which ->
                negativeButtonAction?.invoke()
            }
            .show()
    }

    protected fun showToast(message: String?) = message?.let { messageNotNull ->
        Toast.makeText(context, messageNotNull, Toast.LENGTH_SHORT).show()
    } ?: run {
        Log.d(TAG, "toast message is null")
    }

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B
}