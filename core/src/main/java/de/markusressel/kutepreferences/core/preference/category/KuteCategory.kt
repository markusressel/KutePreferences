package de.markusressel.kutepreferences.core.preference.category

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord

/**
 * The default implementation of a KutePreference Category
 */
open class KuteCategory(
    override val key: Int,
    val icon: Drawable? = null,
    override val title: String,
    override val description: String = "",
    override val children: List<KutePreferenceListItem> = emptyList(),
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
) : KutePreferenceCategory {

    override fun search(searchTerm: String): Boolean {
        return listOf(title, description).containsAnyWord(searchTerm)
    }

}
