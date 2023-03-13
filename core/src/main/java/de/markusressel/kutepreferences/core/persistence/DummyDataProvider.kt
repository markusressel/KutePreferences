package de.markusressel.kutepreferences.core.persistence

import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DummyDataProvider : KutePreferenceDataProvider {

    private val memoryStorage = mutableMapOf<Any, Any>()

    override fun <DataType : Any> storeValue(
        kutePreference: KutePreferenceItem<DataType>,
        newValue: DataType
    ) {
        storeValueUnsafe(kutePreference.key, newValue)
    }

    override fun <DataType : Any> storeValueUnsafe(key: Int, newValue: DataType) {
        memoryStorage[key] = newValue
    }

    override fun <DataType : Any> getValueFlow(kutePreference: KutePreferenceItem<DataType>): StateFlow<DataType> {
        // TODO: this will never update
        return MutableStateFlow(getValue(kutePreference))
    }

    override fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType {
        return getValueUnsafe(kutePreference.key, kutePreference.getDefaultValue())
    }

    override fun <DataType : Any> getValueUnsafe(key: Int, defaultValue: DataType): DataType {
        @Suppress("UNCHECKED_CAST")
        return memoryStorage.getOrDefault(key, defaultValue) as DataType
    }

}