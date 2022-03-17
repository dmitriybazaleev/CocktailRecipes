package com.baza.cocktailrecipe.presentation.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.airbnb.lottie.LottieAnimationView
import com.baza.cocktailrecipe.R

class PlaceHolderView : LinearLayout {

    private var mPlaceHolderText: TextView? = null
    private var mLottieImage: LottieAnimationView? = null

    constructor(context: Context) : this(context, null) {
        bind(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        bind(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
        0
    ) {
        bind(attrs, defStyleAttr)
    }

    private fun bind(attrs: AttributeSet?, defStyleAttr: Int) {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater).inflate(R.layout.view_place_holder, this)

        mPlaceHolderText = findViewById(R.id.txv_place_holder)
        mLottieImage = findViewById(R.id.ivm_place_holder)

        getAttrs(attrs, defStyleAttr)
    }

    private fun getAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let { attrNotNull ->
            var typedArray: TypedArray? = null
            try {
                typedArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.PlaceHolderView,
                    defStyleAttr,
                    0
                )
                setPlaceHolderText(
                    typedArray.getString(
                        R.styleable.PlaceHolderView_placeholderText
                    )
                )
                setLottieAnim(
                    typedArray.getResourceId(
                        R.styleable.PlaceHolderView_placeholderLottieRaw,
                        -1
                    )
                )

            } finally {
                typedArray?.recycle()
            }
        }
    }

    fun setPlaceHolderText(placeHolderText: String?) =
        placeHolderText?.let { phTextNotNull ->
            mPlaceHolderText?.text = phTextNotNull
        }

    fun setPlaceHolderText(@StringRes placeHolderText: Int) {
        try {
            val strPlaceHolder = context.getString(placeHolderText)
            setPlaceHolderText(strPlaceHolder)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setLottieAnim(@RawRes rawRes: Int) {
        if (rawRes != -1) {
            mLottieImage?.setAnimation(rawRes)
        }
    }

    fun setLottieAnim(anim: Animation?) = anim?.let { animNotNull ->
        mLottieImage?.setAnimation(animNotNull)
    }

    fun setLottieAnim(assetName: String?) = assetName?.let { assetNameNotNull ->
        mLottieImage?.setAnimation(assetNameNotNull)
    }
}