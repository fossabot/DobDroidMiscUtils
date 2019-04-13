package ro.dobrescuandrei.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object Keyboard
{
    fun close(on: Context) = close(on.asActivity()!!)

    fun close(on: Activity)
    {
        (on.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(on
                .window
                .decorView
                .rootView
                .windowToken, 0)
    }

    fun open(on: Context) = open(on.asActivity()!!)

    fun open(on: Activity)
    {
        (on.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInputFromWindow(on
                .window
                .decorView
                .rootView
                .windowToken,
                InputMethodManager.SHOW_FORCED, 0)
    }
}
