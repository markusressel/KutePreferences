package de.markusressel.kutepreferences.core.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Default implementation of a KutePreferenceDataProvider using Jetpack Datastore as a backend
 */
@OptIn(DelicateCoroutinesApi::class)
class DataStoreKutePreferenceDataProvider(val context: Context) : KutePreferenceDataProvider {

    private val dataStoreAdapter = DataStoreAdapter(context)

    override fun <DataType : Any> storeValue(
        kutePreference: KutePreferenceItem<DataType>,
        newValue: DataType
    ) {
        GlobalScope.launch {
            dataStoreAdapter.storeValue(context.getString(kutePreference.key), newValue)
        }
    }

    override fun <DataType : Any> storeValueUnsafe(key: Int, newValue: DataType) {
        GlobalScope.launch {
            dataStoreAdapter.storeValue(context.getString(key), newValue)
        }
    }

    override fun <DataType : Any> getValueFlow(kutePreference: KutePreferenceItem<DataType>): StateFlow<DataType> {
        return runBlocking {
            dataStoreAdapter.getValueFlow(
                key = context.getString(kutePreference.key),
                defaultValue = kutePreference.getDefaultValue()
            ).stateIn(GlobalScope)
        }
    }

    override fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType {
        return runBlocking {
            dataStoreAdapter.getValue(context.getString(kutePreference.key), kutePreference.getDefaultValue())
        }
    }

    override fun <DataType : Any> getValueUnsafe(key: Int, defaultValue: DataType): DataType {
        return runBlocking {
            dataStoreAdapter.getValue(context.getString(key), defaultValue)
        }
    }

}

class DataStoreAdapter(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kute_preferences")

    private var gson: Gson = Gson()


    suspend fun <T : Any> storeValue(key: String, value: T) {
        try {
            val keyTyped = createKeyFor(key, value::class.java)
            context.dataStore.edit { settings ->
                settings[keyTyped as Preferences.Key<T>] = value
            }
        } catch (e: IllegalArgumentException) {
            val keyTyped = stringPreferencesKey(key)
            val json = gson.toJson(value, value::class.java)
            context.dataStore.edit { settings ->
                settings[keyTyped] = json
            }
        }
    }

    suspend fun <T : Any> getValue(key: String, defaultValue: T): T {
        return try {
            val keyTyped = createKeyFor(key, defaultValue::class.java)
            context.dataStore.data.first()[keyTyped] ?: defaultValue
        } catch (e: IllegalArgumentException) {
            val keyTyped = stringPreferencesKey(key)
            @Suppress("UNCHECKED_CAST")
            gson.fromJson<Any>(context.dataStore.data.first()[keyTyped] as String, defaultValue::class.java) as T
        }

    }

    fun <T : Any> getValueFlow(key: String, defaultValue: T): Flow<T> {
        return try {
            val keyTyped = createKeyFor(key, defaultValue::class.java)
            context.dataStore.data.map { settings ->
                settings[keyTyped] ?: defaultValue
            }
        } catch (e: IllegalArgumentException) {
            val keyTyped = stringPreferencesKey(key)
            @Suppress("UNCHECKED_CAST")
            context.dataStore.data.map { settings ->
                settings[keyTyped]
            }.map {
                if (it == null) {
                    return@map defaultValue
                } else {
                    @Suppress("UNCHECKED_CAST")
                    gson.fromJson<Any>(it, defaultValue::class.java) as T
                }
            }
        }
    }

    private fun <T> createKeyFor(
        key: String,
        clazz: Class<T>
    ): Preferences.Key<T> {
        return when (clazz) {
            Boolean::class.javaPrimitiveType,
            Boolean::class.javaObjectType -> booleanPreferencesKey(key)
            Int::class.javaPrimitiveType,
            Int::class.javaObjectType -> intPreferencesKey(key)
            Float::class.javaPrimitiveType,
            Float::class.javaObjectType -> floatPreferencesKey(key)
            Long::class.javaPrimitiveType,
            Long::class.javaObjectType -> longPreferencesKey(key)
            Double::class.javaPrimitiveType,
            Double::class.javaObjectType -> doublePreferencesKey(key)
            String::class.javaPrimitiveType,
            String::class.javaObjectType -> stringPreferencesKey(key)
            else -> throw IllegalArgumentException("Type is not supported: $clazz")
        } as Preferences.Key<T>
    }

}