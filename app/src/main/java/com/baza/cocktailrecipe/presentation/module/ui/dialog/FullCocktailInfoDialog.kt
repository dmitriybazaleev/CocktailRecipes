package com.baza.cocktailrecipe.presentation.module.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.baza.cocktailrecipe.R
import com.baza.cocktailrecipe.databinding.FragmentFullCocktailInfoBinding
import com.baza.cocktailrecipe.presentation.module.data.entity.DrinkEntity
import com.baza.cocktailrecipe.presentation.module.data.entity.IngredientEntity
import com.baza.cocktailrecipe.presentation.module.ui.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FullCocktailInfoDialog : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "fullCocktailInfo"

        const val DRINK_PARAMS_KEY = "drinkParamsKey"
        const val INGREDIENT_PARAMS_KEY = "ingredientParamsKey"


        @JvmStatic
        fun create(drinkEntity: DrinkEntity): FullCocktailInfoDialog {
            val args = bundleOf(DRINK_PARAMS_KEY to drinkEntity)
            val instance = FullCocktailInfoDialog()
            instance.arguments = args

            return instance
        }

        @JvmStatic
        fun create(ingredientEntity: IngredientEntity): FullCocktailInfoDialog {
            val args = bundleOf(INGREDIENT_PARAMS_KEY to ingredientEntity)
            val instance = FullCocktailInfoDialog()
            instance.arguments = args

            return instance
        }
    }

    private var _binding: FragmentFullCocktailInfoBinding? = null
    private val binding: FragmentFullCocktailInfoBinding?
        get() = _binding

    private var behavior: BottomSheetBehavior<View>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullCocktailInfoBinding.inflate(inflater, container, false)

        return requireNotNull(_binding?.root)
    }

    override fun getTheme(): Int = R.style.WhiteBottomSheetDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgs()
        addListeners()
    }

    private fun addListeners() {
        binding?.ibCocktailDialogClose?.setOnClickListener {
            dismiss()
        }
    }

    fun setOnButtonClickListener(
        action: (v: View) -> Unit
    ) {
        binding?.btnFullCocktailSave?.setOnClickListener { view ->
            action.invoke(view)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (manager.findFragmentByTag(tag) != null) return
        super.show(manager, tag)
    }

    private fun getArgs() {
        arguments?.let { args ->
            if (args[DRINK_PARAMS_KEY] != null) {
                val drinkEntity = args[DRINK_PARAMS_KEY] as? DrinkEntity
                Log.d(TAG, "DrinkEntity: $drinkEntity")
                binding?.ivFullCocktail?.loadGlide(drinkEntity?.strDrinkThumb)
                binding?.txvFullCocktailDescr?.setTextOrHide(drinkEntity?.strInstruction)
                binding?.txvFullCocktailAlcoholic?.setTextOrHide(drinkEntity?.strAlcoholic)
                binding?.txvFullCocktailCategory?.setTextOrHide(drinkEntity?.strCategory)
            }

            if (args[INGREDIENT_PARAMS_KEY] != null) {
                val ingredientEntity = args[INGREDIENT_PARAMS_KEY] as? IngredientEntity
                Log.d(TAG, "Ingredient entity: $ingredientEntity")
                // TODO: 19.03.2022 Replace with your own action!
                return
            }
        }
    }
}