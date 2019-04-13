package ro.dobrescuandrei.utils

import android.view.View

fun View.setOnLongClickListener(shouldConsumeTouchEvent : Boolean = false, listener : () -> (Unit)) {
    setOnLongClickListener {
        listener()
        return@setOnLongClickListener /*isTouchEventConsumed=*/ shouldConsumeTouchEvent
    }
}
