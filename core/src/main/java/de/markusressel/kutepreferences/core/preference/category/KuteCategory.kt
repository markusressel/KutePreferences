package de.markusressel.kutepreferences.core.preference.category

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.KuteSearchContent

/**
 * The default implementation of a KutePreference Category
 */
open class KuteCategory(
    override val key: String,
    val icon: Drawable? = null,
    override val title: String,
    override val description: String = "",
    override val children: List<KutePreferenceListItem> = emptyList(),
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
    override val visibleWhen: KuteCondition? = null,
    override val enabledWhen: KuteCondition? = null,
    override val keywords: List<String> = emptyList(),
) : KutePreferenceCategory {

    override val searchContent
        get() = KuteSearchContent(title = title, keywords = keywords, description = description)

}
