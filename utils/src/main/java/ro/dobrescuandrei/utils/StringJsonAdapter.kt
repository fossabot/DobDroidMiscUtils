package ro.dobrescuandrei.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class StringJsonAdapter
(
    val defaultValue : String?
) : TypeAdapter<String>()
{
    override fun write(out: JsonWriter, value: String?)
    {
        if (value==defaultValue)
            out.nullValue()
        else out.value(value)
    }

    override fun read(`in`: JsonReader): String?
    {
        if (`in`.peek()==JsonToken.NULL)
        {
            `in`.nextNull()
            return defaultValue
        }

        return `in`.nextString()
    }
}
