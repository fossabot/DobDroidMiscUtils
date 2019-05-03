package ro.dobrescuandrei.dobdroidmiscutils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import ro.dobrescuandrei.utils.*

class SampleCustomView : View
{
    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        val args : TypedArray=context.obtainStyledAttributes(attrs, R.styleable.SampleCustomView)
        val someBoolean : Boolean=args.getBoolean(R.styleable.SampleCustomView_scv_someBoolean)?:false
        val someInt : Int=args.getInt(R.styleable.SampleCustomView_scv_someInt)?:0
        val someFloat : Float=args.getFloat(R.styleable.SampleCustomView_scv_someFloat)?:0f
        val someColor : Color=args.getKolor(R.styleable.SampleCustomView_scv_someColor)?:Colors.Black
        val someDimension : Float=args.getDimension(R.styleable.SampleCustomView_scv_someDimension)?:0f
        val someDimensionInPx : Int=args.getDimensionInPixels(R.styleable.SampleCustomView_scv_someDimension)?:0
        val someResourceId : Int?=args.getResourceId(R.styleable.SampleCustomView_scv_someResource)
        args.recycle()
    }
}
