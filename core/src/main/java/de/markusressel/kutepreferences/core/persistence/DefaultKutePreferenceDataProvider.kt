package de.markusressel.kutepreferences.core.persistence

import android.content.Context
import android.content.SharedPreferences
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation of a KutePreferenceDataProvider using a SharedPreferences backed store
 */
class DefaultKutePreferenceDataProvider(context: Context) : KutePreferenceDataProvider {

    private val appContext = context.applicationContext

    // loading the file blocks, so it is deferred until the first access, which is done by load() if possible
    private val preferences: SharedPreferences by lazy {
        appContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).also {
            it.registerOnSharedPreferenceChangeListener(changeListener)
        }
    }

    private class ValueFlow<T : Any>(val defaultValue: T, val flow: MutableStateFlow<T>)

    private val valueFlows = ConcurrentHashMap<String, ValueFlow<*>>()

    // SharedPreferences only keeps a weak reference to its listeners, so this has to be a field
    private val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            // all values have been removed
            null -> valueFlows.keys.forEach(::refreshFlow)
            else -> refreshFlow(key)
        }
    }

    override suspend fun load() {
        withContext(Dispatchers.IO) {
            preferences
        }
    }

    override fun <DataType : Any> storeValue(
        kutePreference: KutePreferenceItem<DataType>,
        newValue: DataType
    ) = storeValueUnsafe(kutePreference.key, newValue)

    override fun <DataType : Any> storeValueUnsafe(key: String, newValue: DataType) {
        preferences.edit().apply {
            when (newValue) {
                is Boolean -> putBoolean(key, newValue)
                is String -> putString(key, newValue)
                is Int -> putInt(key, newValue)
                is Float -> putFloat(key, newValue)
                is Long -> putLong(key, newValue)
                else -> putString(key, PreferenceValueCodec.encode(newValue))
            }
        }.apply()
        refreshFlow(key)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <DataType : Any> getValueFlow(kutePreference: KutePreferenceItem<DataType>): StateFlow<DataType> {
        val valueFlow = valueFlows.getOrPut(kutePreference.key) {
            val defaultValue = kutePreference.getDefaultValue()
            ValueFlow(defaultValue, MutableStateFlow(read(kutePreference.key, defaultValue)))
        } as ValueFlow<DataType>
        return valueFlow.flow.asStateFlow()
    }

    override fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType {
        return read(kutePreference.key, kutePreference.getDefaultValue())
    }

    override fun <DataType : Any> getValueUnsafe(key: String, defaultValue: DataType): DataType {
        return read(key, defaultValue)
    }

    private fun refreshFlow(key: String) {
        valueFlows[key]?.let { refresh(key, it) }
    }

    private fun <T : Any> refresh(key: String, valueFlow: ValueFlow<T>) {
        valueFlow.flow.value = read(key, valueFlow.defaultValue)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> read(key: String, defaultValue: T): T {
        val stored = preferences.all[key] ?: return defaultValue
        return when {
            defaultValue.isNativeType() -> if (stored.javaClass == defaultValue.javaClass) stored as T else defaultValue
            stored is String -> PreferenceValueCodec.decodeOrDefault(key, stored, defaultValue)
            else -> defaultValue
        }
    }

    private fun Any.isNativeType() = this is Boolean || this is String || this is Int || this is Float || this is Long

    private companion object {
        const val PREFERENCES_NAME = "kute_preferences"
    }

}
