package de.markusressel.kutepreferences.core.persistence

import android.support.annotation.StringRes
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

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
     * WARNING: This method can break type safety!
     *
     * Stores a new value for the given key
     *
     * WARNING: Note that type safety is only guaranteed by the correct type of the new value.
     * When accessing this method directly (and not through a KutePreferenceItem)
     * you have to know the resulting type by yourself.
     *
     * @param key the preference key
     * @param newValue the new value to store
     */
    fun <DataType : Any> storeValueUnsafe(@StringRes key: Int, newValue: DataType)

    /**
     * @return the currently persisted value
     */
    fun <DataType : Any> getValue(kutePreference: KutePreferenceItem<DataType>): DataType

    /**
     * Gets the currently persisted value for the given key, or the default value if no stored value was found.
     *
     * WARNING: Note that type safety is only guaranteed by the correct type usage of the passed in default value.
     * When accessing this method directly (and not through a KutePreferenceItem)
     * be sure to pass in the correct default value to ensure type correctness.
     *
     * @param key the preference key
     * @param defaultValue the default value of the preference
     * @return the currently persisted value
     */
    fun <DataType : Any> getValueUnsafe(@StringRes key: Int, defaultValue: DataType): DataType

}