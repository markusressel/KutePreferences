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
     *
     * @param oldValue the value before it changed
     * @param newValue the value after it changed
     * @param byUser true, if the change was initiated by the user, false otherwise
     */
    fun onCurrentValueChanged(oldValue: DataType?, newValue: DataType?, byUser: Boolean)

    /**
     * Restore the current input value to the persisted value of the associated preference item.
     * If there is no persisted value the default value will be used instead.
     */
    fun restore()

    /**
     * Save the current input value using the data provider
     */
    fun save()

    /**
     * Reset the current dialog input value to the default value
     */
    fun resetToDefault()

    /**
     * Closes the dialog without any save actions
     */
    fun dismiss()

    /**
     * Show this dialog
     */
    fun show(context: Context)

}