package de.markusressel.kutepreferences.core.preference.category

import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceClickListener

/**
 * Interface for a KutePreference section that groups preferences within a page
 */
interface KutePreferenceSection : KutePreferenceListItem, KutePreferenceClickListener, KuteSearchProvider {

    /**
     * The title of this section
     */
    val title: String

    /**
     * A list of all the preferences in this section
     */
    val preferenceItems: List<KutePreferenceListItem>

}