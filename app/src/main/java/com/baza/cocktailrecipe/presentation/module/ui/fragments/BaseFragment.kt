package com.baza.cocktailrecipe.presentation.module.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.baza.cocktailrecipe.presentation.module.ui.BackPressedHandler
import com.baza.cocktailrecipe.presentation.module.ui.MainActivity
import com.baza.cocktailrecipe.presentation.module.ui.dialog.ActionDialog
import com.baza.cocktailrecipe.presentation.module.ui.viewmodel.BaseViewModel
import com.baza.cocktailrecipe.presentation.navigation.NavArguments

abstract class BaseFragment<B : ViewBinding> : Fragment(), BackPressedHandler {

    companion object {
        private const val TAG = "baseFragment"
    }

    private var _binding: B? = null
    protected val binding: B?
        get() = _binding

    private var _act: MainActivity? = null
    protected val act: MainActivity?
        get() = _act

    private val viewModel by viewModels<BaseViewModel>()

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

    fun showActionDialog(
        title: String?,
        message: String?,
        positiveButtonText: String? = null,
        positiveButtonAction: ((v: View) -> Unit)? = null,
        negativeButtonText: String? = null,
        negativeButtonAction: ((v: View) -> Unit)? = null
    ) {
        ActionDialog.Builder(requireContext(), childFragmentManager)
            .setLabel(title)
            .setMessage(message)
            .setNegativeButton(negativeButtonText, negativeButtonAction)
            .setPositiveButton(positiveButtonText, positiveButtonAction)
            .show()
    }

    fun showActionDialog(
        @StringRes titleRes: Int,
        @StringRes messageRes: Int,
        @StringRes positiveButtonTextRes: Int,
        positiveButtonAction: ((v: View) -> Unit)? = null,
        negativeButtonText: Int,
        negativeButtonAction: ((v: View) -> Unit)? = null
    ) {
        showToast("privet")
        ActionDialog.Builder(requireContext(), childFragmentManager)
            .setLabel(titleRes)
            .setMessage(messageRes)
            .setPositiveButton(positiveButtonTextRes, positiveButtonAction)
            .setNegativeButton(negativeButtonText, negativeButtonAction)
            .show()
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

    fun addFragment(destinationId: Int) = act?.navigator?.addFragment(destinationId)

    fun addFragment(destinationId: Int, vararg navArguments: NavArguments) =
        act?.navigator?.addFragment(destinationId, *navArguments)

    fun addFragment(
        destinationId: Int,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int,
        @AnimRes popEnterAnim: Int,
        @AnimRes popExitAnim: Int
    ) = act?.navigator?.addFragment(destinationId, enterAnim, exitAnim, popEnterAnim, popExitAnim)

    fun addFragment(
        destinationId: Int,
        @AnimRes enterAnim: Int,
        @AnimRes exitAnim: Int,
        @AnimRes popEnterAnim: Int,
        @AnimRes popExitAnim: Int,
        vararg argument: NavArguments
    ) = act?.navigator?.addFragment(
        destinationId,
        enterAnim,
        exitAnim,
        popEnterAnim,
        popExitAnim,
        *argument
    )

    fun getCurrentFragment() = act?.navigator?.getCurrentFragment()

    fun getController(): NavController? = act?.navigator?.getController()


    fun popBackstack() = act?.navigator?.popBackStack()

    override fun onBackPressed(): Boolean = false

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> B
}