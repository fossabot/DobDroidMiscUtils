package ro.dobrescuandrei.utils

import android.annotation.SuppressLint
import com.github.debop.kodatimes.toDateTime
import com.github.debop.kodatimes.toLocalDate
import com.github.debop.kodatimes.toLocalTime
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import java.text.SimpleDateFormat
import java.util.*

val NilDateTime = DateTime(0L)
val NilLocalDate = LocalDate(0L)
val NilLocalTime = LocalTime(0L)

fun DateTime.isNil() = millis==0L
fun LocalDate.isNil() = this==NilLocalDate
fun LocalTime.isNil() = this==NilLocalTime

fun DateTime.isNotNil() = !isNil()
fun LocalDate.isNotNil() = !isNil()
fun LocalTime.isNotNil() = !isNil()

fun DateTime.format() = DateTimeFormatter(this)
fun LocalTime.format() = this.toDateTimeToday().format()
fun LocalDate.format() = this.toDateTimeAtCurrentTime().format()

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

fun LocalDate.asCalendar() : Calendar = this.toDateTimeAtCurrentTime().asCalendar()
fun LocalTime.asCalendar() : Calendar = this.toDateTimeToday().asCalendar()

fun Calendar.asDateTime() : DateTime = DateTime(timeInMillis)
fun Calendar.asLocalDate() : LocalDate = this.asDateTime().toLocalDate()
fun Calendar.asLocalTime() : LocalTime = this.asDateTime().toLocalTime()

fun max(dateTime1 : DateTime, dateTime2 : DateTime) : DateTime  =
    DateTime(Math.max(dateTime1.millis, dateTime2.millis))
fun max(date1 : LocalDate, date2 : LocalDate) : LocalDate =
    max(date1.toDateTimeAtStartOfDay(), date2.toDateTimeAtStartOfDay()).toLocalDate()
fun max(time1 : LocalTime, time2 : LocalTime) : LocalTime =
    max(time1.toDateTimeToday(), time2.toDateTimeToday()).toLocalTime()
fun min(dateTime1 : DateTime, dateTime2 : DateTime) : DateTime  =
    DateTime(Math.min(dateTime1.millis, dateTime2.millis))
fun min(date1 : LocalDate, date2 : LocalDate) : LocalDate =
    min(date1.toDateTimeAtStartOfDay(), date2.toDateTimeAtStartOfDay()).toLocalDate()
fun min(time1 : LocalTime, time2 : LocalTime) : LocalTime =
    min(time1.toDateTimeToday(), time2.toDateTimeToday()).toLocalTime()

class DateTimeJsonAdapter
(
    format : String,
    val formatter : SimpleDateFormat = SimpleDateFormat(format)
) : TypeAdapter<DateTime>()
{
    override fun write(out: JsonWriter, value: DateTime?)
    {
        if (value==null||value==NilDateTime)
            out.nullValue()
        else out.value(formatter.format(value.toDate()))
    }

    override fun read(`in`: JsonReader): DateTime
    {
        if (`in`.peek()== JsonToken.NULL)
        {
            `in`.nextNull()
            return NilDateTime
        }

        return formatter.parse(`in`.nextString()).toDateTime()
    }
}

class LocalTimeJsonAdapter
(
    format : String,
    val formatter : SimpleDateFormat = SimpleDateFormat(format)
) : TypeAdapter<LocalTime>()
{
    @SuppressLint("SimpleDateFormat")
    val apiDateFormatter = SimpleDateFormat("HH:mm")

    override fun write(out: JsonWriter, value: LocalTime?)
    {
        if (value==null)
            out.nullValue()
        else out.value(formatter.format(value.toDateTimeToday().toDate()))
    }

    override fun read(`in`: JsonReader): LocalTime?
    {
        if (`in`.peek()== JsonToken.NULL)
        {
            `in`.nextNull()
            return null
        }

        return formatter.parse(`in`.nextString()).toLocalTime()
    }
}

class LocalDateJsonAdapter
(
    format : String,
    val formatter : SimpleDateFormat = SimpleDateFormat(format)
) : TypeAdapter<LocalDate>()
{
    @SuppressLint("SimpleDateFormat")
    val apiDateFormatter = SimpleDateFormat("yyyy-MM-dd")

    override fun write(out: JsonWriter, value: LocalDate?)
    {
        if (value==null)
            out.nullValue()
        else out.value(formatter.format(value.toDate()))
    }

    override fun read(`in`: JsonReader): LocalDate?
    {
        if (`in`.peek()== JsonToken.NULL)
        {
            `in`.nextNull()
            return null
        }

        return formatter.parse(`in`.nextString()).toLocalDate()
    }
}
