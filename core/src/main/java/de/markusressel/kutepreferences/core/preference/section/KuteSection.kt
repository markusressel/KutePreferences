package de.markusressel.kutepreferences.core.preference.section

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord


/**
 * The default implementation of a KutePreference Section
 */
open class KuteSection(
    override val key: Int,
    override val title: String,
    override val children: List<KutePreferenceListItem>,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null
) : KutePreferenceSection {

    override fun search(searchTerm: String): Boolean {
        return listOf(title).containsAnyWord(searchTerm)
    }


}