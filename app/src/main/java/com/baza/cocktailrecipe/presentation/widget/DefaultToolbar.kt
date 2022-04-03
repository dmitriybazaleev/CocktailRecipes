package com.baza.cocktailrecipe.presentation.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.baza.cocktailrecipe.R
import java.lang.Exception

class DefaultToolbar : LinearLayout {

    private var mToolbarText: TextView? = null
    private var mToolbarImageBack: ImageButton? = null

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
                as LayoutInflater).inflate(R.layout.view_default_toolbar, this)

        mToolbarText = findViewById(R.id.txv_toolbar)
        mToolbarImageBack = findViewById(R.id.ib_toolbar_back)

        getAttrs(attrs, defStyleAttr)
    }

    private fun getAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs?.let { attrNotNull ->
            var typedArray: TypedArray? = null
            try {
                typedArray = context.obtainStyledAttributes(
                    attrNotNull,
                    R.styleable.DefaultToolbar,
                    defStyleAttr,
                    0
                )
                setToolbarText(
                    typedArray.getString(
                        R.styleable.DefaultToolbar_toolbarText
                    )
                )
                setBackstackButtonVisibility(typedArray.getBoolean(
                    R.styleable.DefaultToolbar_includeBack,
                    true
                ))

            } finally {
                typedArray?.recycle()
            }
        }
    }

    fun setBackstackButtonVisibility(isShow: Boolean) {
        mToolbarImageBack?.isVisible = isShow
    }

    fun setToolbarText(text: String?) = text?.let {
        mToolbarText?.text = text
    }

    fun setToolbarText(@StringRes res: Int) = try {
        val strRes = context.getString(res)
        setToolbarText(strRes)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    fun setOnBackButtonClick(listener: (v: View) -> Unit) {
        mToolbarImageBack?.setOnClickListener {
            listener.invoke(it)
        }
    }
}