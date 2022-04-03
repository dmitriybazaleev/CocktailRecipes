package com.baza.cocktailrecipe.presentation.module.ui.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View

object BlurKit {

    const val FULL_SCALE = 1f

    private var rs: RenderScript? = null

    fun create(context: Context) {
        if (rs == null)
            rs = RenderScript.create(context.applicationContext)
    }

    fun blur(src: Bitmap?, radius: Int): Bitmap? {
        val input = Allocation.createFromBitmap(rs, src)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(radius.toFloat())
        script.setInput(input)
        script.forEach(output)
        output.copyTo(src)
        return src
    }

    fun blur(src: View, radius: Int): Bitmap? {
        val bitmap = getBitmapForView(src)
        return blur(bitmap, radius)
    }

    fun fastBlur(src: View, radius: Int, downscaleFactor: Float): Bitmap? {
        val bitmap = getBitmapForView(src, downscaleFactor)
        return blur(bitmap, radius)
    }

    private fun getBitmapForView(src: View, downscaleFactor: Float): Bitmap {
        val bitmap = Bitmap.createBitmap(
            (src.width * downscaleFactor).toInt(),
            (src.height * downscaleFactor).toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val matrix = Matrix()
        matrix.preScale(downscaleFactor, downscaleFactor)
        canvas.setMatrix(matrix)
        src.draw(canvas)
        return bitmap
    }

    private fun getBitmapForView(src: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            src.width,
            src.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        src.draw(canvas)
        return bitmap
    }
}