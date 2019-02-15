package de.markusressel.kutepreferences.core.preference.category

import de.markusressel.kutepreferences.core.KutePreferenceListItem

/**
 * Interface for a KutePreference category that holds a set preferences
 */
interface KutePreferenceCategory : KutePreferenceListItem, KuteParent {

    /**
     * The title of this category
     */
    val title: String

    /**
     * A short description of the contents of this category
     */
    val description: String

}