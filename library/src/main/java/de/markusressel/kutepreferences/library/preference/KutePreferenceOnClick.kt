package de.markusressel.kutepreferences.library.preference

/**
 * Interface for Preferences
 */
interface KutePreferenceOnClick<DataType : Any> {

    /**
     * Called when a KutePreferenceListItem is clicked
     */
    fun onClick(kutePreference: KutePreferenceItem<DataType>)

}