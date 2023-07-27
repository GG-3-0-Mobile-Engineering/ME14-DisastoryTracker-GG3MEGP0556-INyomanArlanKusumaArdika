package com.frhanklin.disastory.utils

import android.content.Context
import android.graphics.drawable.Drawable

interface ResourceProvider {
    fun getString(resourceId: Int): String
    fun getDrawable(resourceId: Int): Drawable
}

class AndroidResourceProvider(private val context: Context): ResourceProvider {
    override fun getString(resourceId: Int): String {
        return context.getString(resourceId)
    }

    override fun getDrawable(resourceId: Int): Drawable {
        return context.getDrawable(resourceId) as Drawable
    }

}