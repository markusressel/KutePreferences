package de.markusressel.kutepreferences.core

interface KuteSearchProvider {

    /**
     * Retrieves a list of all searchable strings
     */
    fun getSearchableItems(): Set<String>

}
