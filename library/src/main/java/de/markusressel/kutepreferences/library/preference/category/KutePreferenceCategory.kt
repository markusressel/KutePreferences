package de.markusressel.kutepreferences.library.preference.category

import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.preference.KutePreferenceOnClick

/**
 * Interface for a KutePreference category that holds a set preferences
 */
interface KutePreferenceCategory : KutePreferenceListItem, KutePreferenceOnClick<Void> {

    /**
     * The name of this category
     */
    val name: String

    /**
     * A short description of the contents of this category
     */
    val description: String

    /**
     * Get the list of child KutePreferences of this category
     */
    fun getChildren(): List<KutePreferenceListItem>

}