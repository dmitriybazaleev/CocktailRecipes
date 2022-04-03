package com.baza.cocktailrecipe.presentation.module.ui.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class BlurHelper constructor(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {

    private val scaleFactor = 0.3

    init {
        BlurKit.create(context)
    }

    fun blurByUrl(url: String?, onSuccess: (Bitmap?) -> Unit) {
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

    private fun doBlur(resource: Bitmap, onSuccess: (Bitmap?) -> Unit) {
        lifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            val scalableBitmap = Bitmap.createScaledBitmap(
                resource,
                (resource.width * scaleFactor).toInt(),
                (resource.height * scaleFactor).toInt(),
                false
            )

            val canvas = Canvas(scalableBitmap)
            val myPaint = Paint()
            myPaint.color = Color.parseColor("#00FFFFFF")
            canvas.drawRect(
                0f,
                0f,
                (resource.width * scaleFactor).toFloat(),
                (resource.height * scaleFactor).toFloat(),
                myPaint
            )
            try {
                BlurKit.blur(scalableBitmap, 10)

            } catch (e: Exception) {
                e.printStackTrace()
            }

            withContext(Dispatchers.Main) {
                onSuccess(scalableBitmap)
            }
        }
    }
}