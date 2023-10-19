@file:Suppress("UNCHECKED_CAST")

package de.markusressel.kutepreferences.core.persistence

import android.content.Context
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.typedpreferences.PreferenceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Default implementation of a KutePreferenceDataProvider using a SharedPreferences backed store
 */
class DefaultKutePreferenceDataProvider(context: Context) : KutePreferenceDataProvider {

    private val preferenceHandler = PreferenceHandler(context)

    // map of "preference key" -> stateflow
    private val listeners = mutableMapOf<Int, MutableStateFlow<*>>()

    override fun <DataType : Any> storeValue(
        kutePreference: KutePreferenceItem<DataType>,
        newValue: DataType
    ) {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
            .createPreferenceItem(kutePreference.key, kutePreference.getDefaultValue())
        preferenceHandler.setValue(preferenceItem, newValue)
        notifyListeners(kutePreference, newValue)
    }

    override fun <DataType : Any> storeValueUnsafe(key: Int, newValue: DataType) {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
            .createPreferenceItem(key, newValue)

        preferenceHandler.setValue(preferenceItem, newValue)
        notifyListeners(key, newValue)
    }

    override fun <DataType : Any> getValueFlow(kutePreference: KutePreferenceItem<DataType>): StateFlow<DataType> {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
            .createPreferenceItem(kutePreference.key, kutePreference.getDefaultValue())

        val stateFlow = listeners.getOrPut(kutePreference.key) {
            MutableStateFlow(preferenceHandler.getValue(preferenceItem))
        } as MutableStateFlow<DataType>

        return stateFlow
    }

    override fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
            .createPreferenceItem(kutePreference.key, kutePreference.getDefaultValue())
        return preferenceHandler.getValue(preferenceItem)
    }

    override fun <DataType : Any> getValueUnsafe(key: Int, defaultValue: DataType): DataType {
        val preferenceItem: PreferenceItem<DataType> = preferenceHandler
            .createPreferenceItem(key, defaultValue)
        return preferenceHandler.getValue(preferenceItem)
    }

    private fun <DataType : Any> notifyListeners(
        kutePreference: KutePreferenceItem<DataType>,
        newValue: DataType
    ) = notifyListeners(kutePreference.key, newValue)

    private fun <DataType : Any> notifyListeners(
        kutePreferenceKey: Int,
        newValue: DataType
    ) {
        (listeners[kutePreferenceKey] as? MutableStateFlow<DataType>)?.let {
            it.value = newValue
        }
    }

}