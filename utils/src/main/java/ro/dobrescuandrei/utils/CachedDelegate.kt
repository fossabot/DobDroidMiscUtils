package ro.dobrescuandrei.utils

import android.util.SparseArray
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CachedDelegate<R, T>
(
    val delegate : ReadWriteProperty<R, T>
) : ReadWriteProperty<R, T>
{
    companion object
    {
        val cache : SparseArray<Any> = SparseArray()
    }

    override fun getValue(thisRef: R, property: KProperty<*>): T
    {
        val key=delegate.hashCode()
        val cachedValue=cache[key]
        if (cachedValue!=null)
            return cachedValue as T

        val value=delegate.getValue(thisRef, property)
        if (value!=null)
            cache.put(key, value as Any)
        return value
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T)
    {
        val key=delegate.hashCode()
        cache.put(key, value as Any)
        delegate.setValue(thisRef, property, value)
    }
}

fun <R, T> cached(delegate : ReadWriteProperty<R, T>) : ReadWriteProperty<R, T> = CachedDelegate(delegate)
