package de.markusressel.kutepreferences.library.view.edit

import android.content.Context
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem

interface KutePreferenceEditDialog<DataType : Any> {

    /**
     * The default value of this preference
     */
    val preferenceItem: KutePreferenceItem<DataType>

    /**
     * The persisted value of this preference
     */
    var persistedValue: DataType
        get() {
            return preferenceItem
                    .persistedValue
        }
        set(value) {
            preferenceItem
                    .persistedValue = value
        }

    /**
     * Called when the currentValue changes
     * Update your GUI here.
     */
    fun onCurrentValueChanged(oldValue: DataType?, newValue: DataType?)

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

    /**
     * Closes the dialog without any save actions
     */
    fun dismiss()

    /**
     * Show this dialog
     */
    fun show(context: Context)

}