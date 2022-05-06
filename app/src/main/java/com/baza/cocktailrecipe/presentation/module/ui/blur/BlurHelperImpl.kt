package com.baza.cocktailrecipe.presentation.module.ui.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.lang.Exception

class BlurHelperImpl(
    private val context: Context,
) : IBlurHelper {

    companion object {
        const val SCALE_FACTOR = 0.3
        const val DEFAULT_BLUR_COLOR = "#00FFFFFF"
    }

    init {
        BlurKit.create(context)
    }

    override suspend fun blurByUrl(url: String?, onSuccess: (Bitmap?) -> Unit) {
        if (url.isNullOrEmpty()) {
            onSuccess(null)
            return
        }
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    doBlur(resource, onSuccess)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    onSuccess(null)
                }
            })
    }

    @WorkerThread
    private fun doBlur(resource: Bitmap, onSuccess: (Bitmap?) -> Unit) {
        val scalableBitmap = Bitmap.createScaledBitmap(
            resource,
            (resource.width * SCALE_FACTOR).toInt(),
            (resource.height * SCALE_FACTOR).toInt(),
            false
        )

        val canvas = Canvas(scalableBitmap)
        val myPaint = Paint()
        myPaint.color = Color.parseColor(DEFAULT_BLUR_COLOR)
        canvas.drawRect(
            0f,
            0f,
            (resource.width * SCALE_FACTOR).toFloat(),
            (resource.height * SCALE_FACTOR).toFloat(),
            myPaint
        )
        try {
            BlurKit.blur(scalableBitmap, 10)

        } catch (e: Exception) {
            e.printStackTrace()

        }

        onSuccess(scalableBitmap)
    }
}