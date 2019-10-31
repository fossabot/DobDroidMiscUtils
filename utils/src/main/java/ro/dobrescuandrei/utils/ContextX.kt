package ro.dobrescuandrei.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.toActivity() : Activity?
{
    if (this is Activity)
        return this
    if (this is ContextWrapper)
        return baseContext.toActivity()
    return null
}
