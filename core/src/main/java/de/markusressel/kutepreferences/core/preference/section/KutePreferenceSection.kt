package de.markusressel.kutepreferences.core.preference.section

import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.KuteSearchable
import de.markusressel.kutepreferences.core.preference.category.KuteParent

/**
 * Interface for a KutePreference section that groups preferences within a page
 */
interface KutePreferenceSection : KutePreferenceListItem, KuteParent, KuteSearchable {

    /**
     * The title of this section
     */
    val title: String

}