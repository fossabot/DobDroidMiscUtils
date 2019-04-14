package ro.dobrescuandrei.utils

import android.annotation.SuppressLint
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

val NilDateTime = DateTime(0L)

fun DateTime.isNil() = millis==0L
fun DateTime.isNotNil() = !isNil()

fun DateTime.format() = DateTimeFormatter(this)

class DateTimeFormatter
(
    val dateTime : DateTime
)
{
    @SuppressLint("SimpleDateFormat")
    fun formatWith(pattern : String) = SimpleDateFormat(pattern).format(dateTime.toDate())?:""
}

fun DateTime.asCalendar() : Calendar
{
    val calendar= Calendar.getInstance()
    calendar.timeInMillis=millis
    return calendar
}

fun Calendar.asDateTime() : DateTime = DateTime(timeInMillis)

fun max(dateTime1 : DateTime, dateTime2 : DateTime) : DateTime =
    DateTime(Math.max(dateTime1.millis, dateTime2.millis))

fun min(dateTime1 : DateTime, dateTime2 : DateTime) : DateTime =
    DateTime(Math.min(dateTime1.millis, dateTime2.millis))
