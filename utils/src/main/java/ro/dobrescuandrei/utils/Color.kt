package ro.dobrescuandrei.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

//todo add documentation
class Color
(
    val value : Int
)
{
    constructor(code : String)
        : this(android.graphics.Color.parseColor(code))

    constructor(red : Int, green : Int, blue : Int, alpha : Int = 255)
        : this(android.graphics.Color.argb(alpha, red, green, blue))

    val red   : Int get() = android.graphics.Color.red(value)
    val green : Int get() = android.graphics.Color.green(value)
    val blue  : Int get() = android.graphics.Color.blue(value)
    val alpha : Int get() = android.graphics.Color.alpha(value)

    fun isDark() : Boolean
    {
        val darkness=1-(0.299*red+0.587*green+0.114*blue)/255
        return darkness>=0.5
    }

    fun isLight() : Boolean = !isDark()
}

object Colors
{
    val Black = Color(android.graphics.Color.BLACK)
    val White = Color(android.graphics.Color.WHITE)
}

fun Context.getKolor(colorResourceId : Int) : Color =
    Color(ContextCompat.getColor(this, colorResourceId))

fun Resources.getKolor(colorResourceId : Int) : Color =
    Color(getColor(colorResourceId))

fun View.setBackgroundKolor(color : Color) =
    setBackgroundColor(color.value)

fun TextView.setTextKolor(color : Color) =
    setTextColor(color.value)
