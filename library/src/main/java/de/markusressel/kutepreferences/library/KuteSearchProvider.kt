package de.markusressel.kutepreferences.library

interface KuteSearchProvider {

    /**
     * Retrieves a list of all searchable strings
     */
    fun getSearchableItems(): Set<String>

}
