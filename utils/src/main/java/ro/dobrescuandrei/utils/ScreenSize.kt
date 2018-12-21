package ro.dobrescuandrei.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import kotlin.math.max
import kotlin.math.min

object ScreenSize
{
    var width : Int = 0
    var height : Int = 0
    var density : Float = 0.0f

    fun init(withContext : Context)
    {
        val context=withContext
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        width = min(size.x, size.y)
        height = max(size.x, size.y)
        density = context.resources.displayMetrics.density
    }
}