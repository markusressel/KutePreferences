package de.markusressel.kutepreferences.library.preference.category

import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.KuteSearchProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener

/**
 * Interface for a KutePreference category that holds a set preferences
 */
interface KutePreferenceCategory : KutePreferenceListItem, KutePreferenceClickListener, KuteSearchProvider {

    /**
     * The title of this category
     */
    val title: String

    /**
     * A short description of the contents of this category
     */
    val description: String

    /**
     * A list of child items of this category
     */
    val children: List<KutePreferenceListItem>

}