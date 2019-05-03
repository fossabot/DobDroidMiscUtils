package ro.dobrescuandrei.utils

import android.content.res.TypedArray

fun TypedArray.getBoolean(id : Int) : Boolean? =
    if (hasValue(id))
        getBoolean(id, false)
    else null

fun TypedArray.getInt(id : Int) : Int? = getInteger(id)

fun TypedArray.getInteger(id : Int) : Int? =
    if (hasValue(id))
        getInteger(id, 0)
    else null

fun TypedArray.getFloat(id : Int) : Float? =
    if (hasValue(id))
        getFloat(id, 0f)
    else null

fun TypedArray.getKolor(id : Int) : Color? =
    if (hasValue(id))
        Color(getColor(id, android.graphics.Color.BLACK))
    else null

fun TypedArray.getDimension(id : Int) : Float? =
    if (hasValue(id))
        getDimension(id, 0f)
    else null

fun TypedArray.getDimensionInPixels(id : Int) : Int? =
    if (hasValue(id))
        getDimensionPixelSize(id, 0)
    else null

fun TypedArray.getResourceId(id : Int) : Int? =
    if (hasValue(id))
        getResourceId(id, 0)
    else null
