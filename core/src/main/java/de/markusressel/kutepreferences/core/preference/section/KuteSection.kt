package de.markusressel.kutepreferences.core.preference.section

import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord


/**
 * The default implementation of a KutePreference Section
 */
open class KuteSection(
    override val key: String,
    override val title: String,
    override val children: List<KutePreferenceListItem>,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
    override val visibleWhen: KuteCondition? = null,
    override val enabledWhen: KuteCondition? = null,
) : KutePreferenceSection {

    override fun search(searchTerm: String): Boolean {
        return listOf(title).containsAnyWord(searchTerm)
    }


}