package de.markusressel.kutepreferences.core.preference.category

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem

/**
 * Interface for a KuteParent, which implies that it may have child items
 */
interface KuteParent {

    /**
     * A list of child items of this Parent
     */
    val children: List<KutePreferenceListItem>

    /**
     * A search term which can be used to filter the visible child items.
     *
     * You can use this to enable search integration by setting this value
     * based on the search text input value. To disable filtering, pass [null].
     */
    var searchTerm: String?

}