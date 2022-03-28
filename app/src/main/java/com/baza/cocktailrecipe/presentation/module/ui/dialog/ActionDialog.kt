package com.baza.cocktailrecipe.presentation.module.ui.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.databinding.FragmentActionDialogBinding
import com.baza.cocktailrecipe.presentation.module.ui.loadGlide
import com.baza.cocktailrecipe.presentation.module.ui.setTextOrHide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ActionDialog : BottomSheetDialogFragment() {

    private var binding: FragmentActionDialogBinding? = null

    companion object {
        const val TAG = "actionDialog"

        const val PARAMS_KEY = "actionDialogParams"

        @JvmStatic
        fun create(params: ActionDialogParams): ActionDialog {
            val args = bundleOf(PARAMS_KEY to params)
            val instance = ActionDialog()
            instance.arguments = args
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActionDialogBinding.inflate(layoutInflater, container, false)

        return requireNotNull(binding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
    }

    private fun getArgs() {
        arguments?.let { args ->
            val arguments = args[PARAMS_KEY] as? ActionDialogParams
            arguments?.icon?.let { icon -> binding?.imvDaIcon?.setImageResource(icon) }
            binding?.txvDaTitle?.setTextOrHide(arguments?.actionDialogTitle)
            binding?.txvDaMessage?.setTextOrHide(arguments?.actionDialogMessage)

            arguments?.positiveButtonText?.let { buttonText ->
                binding?.btnPositive?.isVisible = true
                binding?.btnPositive?.text = buttonText
            }

            arguments?.negativeButtonText?.let { buttonText ->
                binding?.btnNegative?.isVisible = true
                binding?.btnNegative?.text = buttonText
            }

            binding?.btnNegative?.setOnClickListener { view ->
                arguments?.negativeButtonAction?.invoke(view)

                dismiss()
            }

            binding?.btnPositive?.setOnClickListener { view ->
                arguments?.positiveButtonAction?.invoke(view)

                dismiss()
            }
            arguments?.icon?.let { icnRes ->
                binding?.imvDaIcon?.loadGlide(ContextCompat.getDrawable(requireContext(), icnRes))
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (manager.findFragmentByTag(tag) != null) return
        super.show(manager, tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun getTheme(): Int = R.style.WhiteBottomSheetDialog

    class Builder constructor(
        private val context: Context,
        private val fm: FragmentManager
    ) {
        private var mLabel: String? = null
        private var mMessage: String? = null
        private var icon: Int? = null

        private var mPositiveButtonText: String? = null
        private var mNegativeButtonText: String? = null

        private var mPositiveButtonAction: ((v: View) -> Unit)? = null
        private var mNegativeButtonAction: ((v: View) -> Unit)? = null

        private fun createArgs() = ActionDialogParams(
            actionDialogTitle = mLabel,
            actionDialogMessage = mMessage,
            icon = icon,
            positiveButtonText = mPositiveButtonText,
            positiveButtonAction = mPositiveButtonAction,
            negativeButtonText = mNegativeButtonText,
            negativeButtonAction = mNegativeButtonAction
        )

        fun setLabel(label: String?): Builder {
            Log.d(TAG, "label: $label")
            mLabel = label

            return this
        }

        fun setLabel(@StringRes labelId: Int): Builder {
            try {
                val labelStr = context.getString(labelId)
                Log.d(TAG, "label: $labelStr")
                mLabel = labelStr

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return this
        }

        fun setMessage(message: String?): Builder {
            Log.d(TAG, "message: $message")
            mMessage = message

            return this
        }

        fun setMessage(@StringRes messageId: Int): Builder {
            try {
                val messageStr = context.getString(messageId)
                Log.d(TAG, "message: $messageStr")
                this.mMessage = messageStr

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return this
        }

        fun setPositiveButton(
            buttonText: String?,
            listener: ((v: View) -> Unit)? = null
        ): Builder {
            Log.d(TAG, "positive button text: $buttonText")
            this.mPositiveButtonText = buttonText
            this.mPositiveButtonAction = listener

            return this
        }

        fun setPositiveButton(
            @StringRes buttonTextRes: Int,
            listener: ((v: View) -> Unit)? = null
        ): Builder {
            try {
                val buttonText = context.getString(buttonTextRes)
                Log.d(TAG, "positive button text: $buttonText")
                this.mPositiveButtonText = buttonText
                this.mPositiveButtonAction = listener

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return this
        }

        fun setNegativeButton(
            buttonText: String?,
            action: ((v: View) -> Unit)? = null
        ): Builder {
            Log.d(TAG, "negative button text: $buttonText")
            this.mNegativeButtonText = buttonText
            this.mNegativeButtonAction = action

            return this
        }

        fun setNegativeButton(
            @StringRes buttonTextRes: Int,
            action: ((v: View) -> Unit)? = null
        ): Builder {
            try {
                val buttonText = context.getString(buttonTextRes)
                Log.d(TAG, "negative button text: $buttonText")
                this.mNegativeButtonText = buttonText
                this.mNegativeButtonAction = action

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return this
        }

        fun setIcon(@DrawableRes iconRes: Int): Builder {
            this.icon = iconRes

            return this
        }

        fun show(): ActionDialog {
            val arguments = createArgs()
            val instance = create(arguments)
            instance.show(fm, TAG)

            return instance
        }
    }
}