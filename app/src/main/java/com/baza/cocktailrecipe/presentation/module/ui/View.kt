package com.baza.cocktailrecipe.presentation.module.ui

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

fun View.visible() {
    if (this.visibility != View.VISIBLE) {
        isVisible = true
    }
}

fun View.invisible() {
    if (this.visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun View.gone() {
    if (this.visibility != View.GONE) {
        isVisible = false
    }
}

fun ImageView.loadGlide(url: String?) {
    url?.let { urlNotNull ->
        Glide.with(this.context)
            .load(urlNotNull)
            .into(this)
    }
}

fun ImageView.loadGlide(drawable: Drawable?) {
    drawable?.let { dr ->
        Glide.with(this.context)
            .asDrawable()
            .load(dr)
            .into(this)
    }
}

fun ImageView.loadGlide(
    url: String?,
    listener: RequestListener<Drawable>? = null
) {
    url?.let { urlNotNull ->
        Glide.with(this.context)
            .load(urlNotNull)
            .listener(listener)
            .into(this)
    }
}

fun ImageView.loadCircleImage(
    url: String?
) {
    url?.let { urlNotNull ->
        Glide.with(this.context)
            .load(urlNotNull)
            .circleCrop()
            .into(this)
    }
}

fun TextView.setTextOrHide(message: String?) {
    message?.let { mess ->
        this.text = mess
    } ?: run {
        this.isVisible = false
    }
}

val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

val Int.sp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()