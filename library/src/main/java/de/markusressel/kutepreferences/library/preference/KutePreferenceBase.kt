package de.markusressel.kutepreferences.library.preference

import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider

/**
 * Base class for KutePreferenceListItem implementations
 */
abstract class KutePreferenceBase<DataType : Any>(
        override var currentValue: DataType? = null,
        /**
         * Data provider for accessing and storing the preference value state
         */
        private val dataProvider: KutePreferenceDataProvider)
    : KutePreferenceItem<DataType> {

    override fun getPersistedValue(): DataType {
        return dataProvider.getValue(this)
    }

    override fun restore() {
        currentValue = dataProvider.getValue(this)
    }

    override fun save() {
        currentValue?.let {
            dataProvider.storeValue(this, it)
        }
    }

    override fun reset() {
        dataProvider.storeValue(this, defaultValue)
        currentValue = defaultValue
    }

}