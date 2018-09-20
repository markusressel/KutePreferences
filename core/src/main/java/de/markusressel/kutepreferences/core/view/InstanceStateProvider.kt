package de.markusressel.kutepreferences.core.view

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.reflect.KProperty

/**
 * Created by Markus on 21.02.2018.
 */
open class InstanceStateProvider<T>(private val savable: Bundle) {

    private var cache: T? = null

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        cache = value
        if (value == null) {
            savable
                    .remove(property.name)
            return
        }
        when (value) {
            is Int -> savable.putInt(property.name, value)
            is Long -> savable.putLong(property.name, value)
            is Float -> savable.putFloat(property.name, value)
            is String -> savable.putString(property.name, value)
            is Bundle -> savable.putBundle(property.name, value)
            is Serializable -> savable.putSerializable(property.name, value)
            is Parcelable -> savable.putParcelable(property.name, value)
        }
    }

    protected fun getAndCache(key: String): T? = cache
            ?: (savable.get(key) as T?).apply { cache = this }

    class Nullable<T>(savable: Bundle) : InstanceStateProvider<T>(savable) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = getAndCache(property.name)
    }

    class NotNull<T>(savable: Bundle, private val defaultValue: T) : InstanceStateProvider<T>(savable) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): T = getAndCache(property.name)
                ?: defaultValue
    }
}