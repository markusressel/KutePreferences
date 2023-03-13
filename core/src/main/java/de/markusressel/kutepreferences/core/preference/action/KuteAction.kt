package de.markusressel.kutepreferences.core.preference.action

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord

/**
 * Implementation of an action preference
 */
open class KuteAction(
    override val key: Int,
    val icon: Drawable? = null,
    val title: String,
    val description: String = "",
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null
) : KutePreferenceListItem {

    override fun search(searchTerm: String) =
        listOf(title, description).containsAnyWord(searchTerm)

}