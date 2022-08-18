package com.project.navermap.util.provider


import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourcesProviderImpl(
    private val context: Context
) : ResourcesProvider {

    override fun getString(resId: Int): String = context.getString(resId)

    override fun getString(resId: Int, vararg formArgs: Any): String =
        context.getString(resId, *formArgs)

    override fun getColor(resId: Int): Int = ContextCompat.getColor(context, resId)

    override fun getColorStateList(resId: Int): ColorStateList =
        AppCompatResources.getColorStateList(context, resId)

    override fun getDrawable(resId: Int): Drawable? = AppCompatResources.getDrawable(context, resId)

}