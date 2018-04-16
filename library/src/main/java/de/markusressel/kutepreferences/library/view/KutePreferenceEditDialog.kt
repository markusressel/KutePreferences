package de.markusressel.kutepreferences.library.view

import de.markusressel.kutepreferences.library.preference.KutePreferenceItem

interface KutePreferenceEditDialog<DataType : Any> {

    /**
     * The default value of this preference
     */
    val preferenceItem: KutePreferenceItem<DataType>

    /**
     * The current value of this preference
     * that might be altered from the persisted value
     */
    var currentValue: DataType?

    /**
     * The persisted value of this preference
     */
    var persistedValue: DataType
        get() {
            return preferenceItem.persistedValue
        }
        set(value) {
            preferenceItem.persistedValue = value
        }

    /**
     * Restore the current value of this KutePreferenceListItem to it's previously persisted value.
     * If there is no persisted value the default value will be used instead.
     */
    fun restore() {
        currentValue = persistedValue
    }

    /**
     * Save the current value of this KutePreferenceListItem into persistence
     */
    fun save() {
        currentValue?.let {
            persistedValue = it
        }
    }

    /**
     * Reset the current value (and persisted) value of this KutePreferenceListItem to the default value
     */
    fun reset() {
        currentValue = preferenceItem.defaultValue
        save()
    }

    /**
     * Show this dialog
     */
    fun show()

}