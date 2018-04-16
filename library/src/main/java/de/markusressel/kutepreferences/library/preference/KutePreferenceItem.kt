package de.markusressel.kutepreferences.library.preference

import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider

/**
 * Interface for Preferences
 */
interface KutePreferenceItem<DataType : Any> : KutePreferenceListItem {

    /**
     * A unique identifier for this KutePreference (used for persistence)
     */
    val key: Int

    /**
     * The name of this preference
     */
    val name: String

    /**
     * The description of this KutePreference according to it's persisted value
     */
    val description: String
        get() {
            return constructDescription(persistedValue)
        }

    /**
     * @param currentValue the current value
     * @return the description for this preference according to the current value
     */
    fun constructDescription(currentValue: DataType): String {
        return "Value: '$currentValue'"
    }

    /**
     * The default value of this preference
     */
    val defaultValue: DataType

    /**
     * The persisted value of this preference
     */
    var persistedValue: DataType
        get() = dataProvider.getValue(this)
        set(value) = dataProvider.storeValue(this, value)

    /**
     * Persistence for this PreferenceItem
     */
    val dataProvider: KutePreferenceDataProvider

    /**
     * Reset the current value (and persisted) value of this KutePreferenceListItem to the default value
     */
    fun reset() {
        persistedValue = defaultValue
    }

}
