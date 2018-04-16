package de.markusressel.kutepreferences.library.persistence

import de.markusressel.kutepreferences.library.preference.KutePreferenceItem

/**
 * Data provider responsible for storing and retrieving
 * preference values from some kind of (hopefully) persisted storage.
 *
 * @param DataType data type returned by the provider
 */
interface KutePreferenceDataProvider {

    /**
     * Stores a new value
     *
     * @param the new value to store
     */
    fun <DataType : Any> storeValue(kutePreference: KutePreferenceItem<DataType>, newValue: DataType)

    /**
     * @return the currently persisted value
     */
    fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType

}