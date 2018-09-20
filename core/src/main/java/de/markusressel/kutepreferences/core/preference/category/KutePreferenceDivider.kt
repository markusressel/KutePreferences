package de.markusressel.kutepreferences.core.preference.category

import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceClickListener

/**
 * Interface for a KutePreference divider that groups preferences within a page
 */
interface KutePreferenceDivider : KutePreferenceListItem, KutePreferenceClickListener, KuteSearchProvider {

    /**
     * The title of this divider
     */
    val title: String

}