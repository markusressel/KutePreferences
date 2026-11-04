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
     * If set, this item is only shown while the condition is met.
     * A hidden item keeps its saved value, and it and its children cannot be found through the search.
     */
    val visibleWhen: KuteCondition?
        get() = null

    /**
     * If set, this item is only usable while the condition is met. Otherwise it is shown as disabled.
     */
    val enabledWhen: KuteCondition?
        get() = null

    /**
     * Search within this [KutePreferenceListItem] for the given [searchTerm]
     *
     * @return true if the item matches the [searchTerm], false otherwise
     */
    fun search(searchTerm: String): Boolean

}
