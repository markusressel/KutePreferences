package de.markusressel.kutepreferences.core.preference

interface KutePreferenceListItem {

    /**
     * A unique identifier for this [KutePreferenceListItem].
     *
     * Persisted items use this as the name of their stored value, so changing it loses the stored value.
     */
    val key: String

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
