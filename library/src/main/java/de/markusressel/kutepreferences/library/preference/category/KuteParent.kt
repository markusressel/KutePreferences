package de.markusressel.kutepreferences.library.preference.category

import de.markusressel.kutepreferences.library.KutePreferenceListItem

/**
 * Interface for a KuteParent, which implies that it may have child items
 */
interface KuteParent {

    /**
     * A list of child items of this Parent
     */
    val children: List<KutePreferenceListItem>

}