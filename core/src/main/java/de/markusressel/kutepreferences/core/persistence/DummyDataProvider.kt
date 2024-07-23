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
        getStateFlow(key, newValue).value = newValue
    }

    override fun <DataType : Any> getValueFlow(kutePreference: KutePreferenceItem<DataType>): StateFlow<DataType> {
        return getStateFlow(kutePreference.key, kutePreference.getDefaultValue()).asStateFlow()
    }

    override fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType {
        return getValueUnsafe(kutePreference.key, kutePreference.getDefaultValue())
    }

    override fun <DataType : Any> getValueUnsafe(key: Int, defaultValue: DataType): DataType {
        @Suppress("UNCHECKED_CAST")
        return memoryStorage.getOrDefault(key, defaultValue) as DataType
    }

    private fun <DataType : Any> getStateFlow(key: Int, defaultValue: DataType): MutableStateFlow<DataType> {
        @Suppress("UNCHECKED_CAST")
        return valueFlows.getOrPut(key) { MutableStateFlow(getValueUnsafe(key, defaultValue)) } as MutableStateFlow<DataType>
    }

}