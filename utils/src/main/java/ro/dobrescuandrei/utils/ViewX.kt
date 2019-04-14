package ro.dobrescuandrei.utils

import android.view.View

fun View.setOnLongKlickListener(shouldConsumeTouchEvent : Boolean = false, listener : (View) -> (Unit)) {
    setOnLongClickListener {
        listener(it)
        return@setOnLongClickListener /*isTouchEventConsumed=*/ shouldConsumeTouchEvent
    }
}
