package ro.dobrescuandrei.utils

import android.view.View

//todo add documentation
fun View.setOnLongKlickListener(shouldConsumeTouchEvent : Boolean = false, listener : () -> (Unit)) {
    setOnLongClickListener {
        listener()
        return@setOnLongClickListener /*isTouchEventConsumed=*/ shouldConsumeTouchEvent
    }
}
