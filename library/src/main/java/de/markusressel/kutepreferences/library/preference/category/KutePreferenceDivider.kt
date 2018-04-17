package de.markusressel.kutepreferences.library.preference.category

import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener

/**
 * Interface for a KutePreference divider that groups preferences within a page
 */
interface KutePreferenceDivider : KutePreferenceListItem, KutePreferenceClickListener {

    /**
     * The name of this category
     */
    val name: String

}