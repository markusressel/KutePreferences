package de.markusressel.kutepreferences.core.persistence

import androidx.compose.runtime.mutableStateMapOf
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A [KutePreferenceDataProvider] that stores all data in memory.
 * Can be used for compose previews and testing purposes.
 */
class DummyDataProvider : KutePreferenceDataProvider {

    private val memoryStorage = mutableStateMapOf<Any, Any>()
    private val valueFlows = mutableMapOf<Any, MutableStateFlow<Any>>()

    override fun <DataType : Any> storeValue(
        kutePreference: KutePreferenceItem<DataType>,
        newValue: DataType
    ) {
        storeValueUnsafe(kutePreference.key, newValue)
    }

    override fun <DataType : Any> storeValueUnsafe(key: Int, newValue: DataType) {
        memoryStorage[key] = newValue
        getStateFlow(key).value = newValue
    }

    override fun <DataType : Any> getValueFlow(kutePreference: KutePreferenceItem<DataType>): StateFlow<DataType> {
        return getStateFlow(kutePreference.key).asStateFlow() as StateFlow<DataType>
    }

    override fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType {
        return getValueUnsafe(kutePreference.key, kutePreference.getDefaultValue())
    }

    override fun <DataType : Any> getValueUnsafe(key: Int, defaultValue: DataType): DataType {
        @Suppress("UNCHECKED_CAST")
        return memoryStorage.getOrDefault(key, defaultValue) as DataType
    }

    private fun getStateFlow(key: Int): MutableStateFlow<Any> {
        return valueFlows.getOrPut(key) { MutableStateFlow(getValueUnsafe(key, Any())) }
    }

}