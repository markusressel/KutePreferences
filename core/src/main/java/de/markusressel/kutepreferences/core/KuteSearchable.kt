package de.markusressel.kutepreferences.core

interface KuteSearchable {

    /**
     * Retrieves a list of all searchable text items.
     *
     * Note: These items will only be used for the search filter. To make sure the found text is
     * highlighted make sure to make use of the [HighlighterFunction] in [KutePreferenceListItem.createEpoxyModel].
     */
    fun getSearchableItems(): Set<String>

}
