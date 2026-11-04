package de.markusressel.kutepreferences.core.persistence

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

// DataStore only allows a single active instance per file, so this has to be a top level property
private val Context.kuteDataStore: DataStore<Preferences> by preferencesDataStore(name = "kute_preferences")

/**
 * Implementation of a KutePreferenceDataProvider using Jetpack Datastore as a backend
 *
 * @param dataStore the store to use
 * @param scope the scope of all reads and writes. Cancelling it stops this provider.
 */
class DataStoreKutePreferenceDataProvider(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
) : KutePreferenceDataProvider {

    constructor(
        context: Context,
        scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
    ) : this(context.applicationContext.kuteDataStore, scope)

    /**
     * The latest content of the store, null until it has been loaded
     */
    private val snapshot: StateFlow<Preferences?> = dataStore.data
        .catch { ex ->
            if (ex !is IOException) throw ex
            Log.e(TAG, "Could not read preferences, using defaults", ex)
            emit(emptyPreferences())
        }
        .stateIn(scope, SharingStarted.Eagerly, null)

    private val valueFlows = ConcurrentHashMap<String, StateFlow<*>>()

    // writes are processed one at a time, in the order they were requested
    private val pendingWrites = Channel<suspend () -> Unit>(Channel.UNLIMITED)

    init {
        scope.launch {
            for (write in pendingWrites) {
                try {
                    write()
                } catch (ex: IOException) {
                    Log.e(TAG, "Could not write preference", ex)
                }
            }
        }
    }

    override suspend fun load() {
        snapshot.filterNotNull().first()
    }

    override fun <DataType : Any> storeValue(
        kutePreference: KutePreferenceItem<DataType>,
        newValue: DataType
    ) = storeValueUnsafe(kutePreference.key, newValue)

    override fun <DataType : Any> storeValueUnsafe(key: String, newValue: DataType) {
        pendingWrites.trySend {
            dataStore.edit { preferences ->
                when (newValue) {
                    is Boolean -> preferences[booleanPreferencesKey(key)] = newValue
                    is Int -> preferences[intPreferencesKey(key)] = newValue
                    is Float -> preferences[floatPreferencesKey(key)] = newValue
                    is Long -> preferences[longPreferencesKey(key)] = newValue
                    is Double -> preferences[doublePreferencesKey(key)] = newValue
                    is String -> preferences[stringPreferencesKey(key)] = newValue
                    else -> preferences[stringPreferencesKey(key)] = PreferenceValueCodec.encode(newValue)
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <DataType : Any> getValueFlow(kutePreference: KutePreferenceItem<DataType>): StateFlow<DataType> {
        return valueFlows.getOrPut(kutePreference.key) {
            val key = kutePreference.key
            val defaultValue = kutePreference.getDefaultValue()
            snapshot.filterNotNull()
                .map { read(it, key, defaultValue) }
                .stateIn(scope, SharingStarted.Eagerly, read(loadedSnapshot(), key, defaultValue))
        } as StateFlow<DataType>
    }

    override fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType {
        return getValueUnsafe(kutePreference.key, kutePreference.getDefaultValue())
    }

    override fun <DataType : Any> getValueUnsafe(key: String, defaultValue: DataType): DataType {
        return read(loadedSnapshot(), key, defaultValue)
    }

    /**
     * Only blocks if the value is requested before [load] has completed.
     */
    private fun loadedSnapshot(): Preferences {
        return snapshot.value ?: runBlocking { snapshot.filterNotNull().first() }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> read(preferences: Preferences, key: String, defaultValue: T): T {
        return when (defaultValue) {
            is Boolean -> readNative(preferences, booleanPreferencesKey(key), defaultValue)
            is Int -> readNative(preferences, intPreferencesKey(key), defaultValue)
            is Float -> readNative(preferences, floatPreferencesKey(key), defaultValue)
            is Long -> readNative(preferences, longPreferencesKey(key), defaultValue)
            is Double -> readNative(preferences, doublePreferencesKey(key), defaultValue)
            is String -> readNative(preferences, stringPreferencesKey(key), defaultValue)
            else -> when (val stored = (preferences.asMap()[stringPreferencesKey(key)])) {
                is String -> PreferenceValueCodec.decodeOrDefault(key, stored, defaultValue)
                else -> defaultValue
            }
        } as T
    }

    /**
     * The key of a store entry only consists of its name, so the stored value can be of a different type.
     */
    private fun readNative(preferences: Preferences, key: Preferences.Key<*>, defaultValue: Any): Any {
        val stored = preferences.asMap()[key] ?: return defaultValue
        return if (stored.javaClass == defaultValue.javaClass) stored else defaultValue
    }

    private companion object {
        const val TAG = "KutePreferences"
    }

}
