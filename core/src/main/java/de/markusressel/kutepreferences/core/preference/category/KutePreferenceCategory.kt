package de.markusressel.kutepreferences.core.preference.category

import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceClickListener

/**
 * Interface for a KutePreference category that holds a set preferences
 */
interface KutePreferenceCategory : KutePreferenceListItem, KutePreferenceClickListener, KuteSearchProvider, KuteParent {

    /**
     * The title of this category
     */
    val title: String

    /**
     * A short description of the contents of this category
     */
    val description: String

}