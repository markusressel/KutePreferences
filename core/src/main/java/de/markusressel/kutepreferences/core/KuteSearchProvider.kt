package de.markusressel.kutepreferences.core

interface KuteSearchProvider {

    /**
     * Retrieves a list of all searchable strings
     */
    fun getSearchableItems(): Set<String>

    /**
     * Highlights all matches that were found
     */
    fun highlightSearchMathes(regex: String)

}
