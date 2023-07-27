package com.frhanklin.disastory.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

class BitmapUtils {

    fun vectorToBitmap(vectorDrawable: VectorDrawableCompat?): Bitmap {
        if (vectorDrawable == null) {
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }

        // Create the Bitmap and the Canvas to draw on it
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        // Draw the Vector XML onto the canvas
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        return bitmap
    }

}