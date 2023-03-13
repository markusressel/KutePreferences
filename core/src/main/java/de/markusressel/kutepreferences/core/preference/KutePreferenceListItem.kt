package de.markusressel.kutepreferences.core.preference

import androidx.annotation.StringRes

interface KutePreferenceListItem {

    /**
     * A unique identifier for this [KutePreferenceListItem]
     */
    @get:StringRes
    val key: Int

    /**
     * OnClick action for this [KutePreferenceListItem]
     */
    val onClick: (() -> Unit)?

    /**
     * OnLongClick action for this [KutePreferenceListItem]
     */
    val onLongClick: (() -> Unit)?

    /**
     * Search within this [KutePreferenceListItem] for the given [searchTerm]
     *
     * @return true if the item matches the [searchTerm], false otherwise
     */
    fun search(searchTerm: String): Boolean

}
