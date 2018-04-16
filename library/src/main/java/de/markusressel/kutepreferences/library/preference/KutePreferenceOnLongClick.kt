package de.markusressel.kutepreferences.library.preference

/**
 * Interface for Preferences
 */
interface KutePreferenceOnLongClick<DataType : Any> {

    /**
     * A unique identifier for this KutePreferenceListItem (used for persistence)
     */
    val key: Int

    /**
     * The name of this preference
     */
    val name: String

    /**
     * The default value of this preference
     */
    val defaultValue: DataType

    /**
     * The current value of this preference
     * that might be altered from the persisted value
     */
    var currentValue: DataType?

    /**
     * The persisted value of this preference
     */
    fun getPersistedValue(): DataType

    /**
     * @param value the currently persisted value
     * @return the description for this preference according to the current value
     */
    fun getDescription(value: DataType): String {
        return "$value"
    }

    /**
     * Restore the current value of this KutePreferenceListItem to it's previously persisted value.
     * If there is no persisted value the default value will be used instead.
     */
    fun restore()

    /**
     * Save the current value of this KutePreferenceListItem into persistence
     */
    fun save()

    /**
     * Reset the current value (and persisted) value of this KutePreferenceListItem to the default value
     */
    fun reset()

}