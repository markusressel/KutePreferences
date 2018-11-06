package de.markusressel.kutepreferences.core

import android.text.Spanned

interface KuteSearchProvider {

    /**
     * Retrieves a list of all searchable strings
     */
    fun getSearchableItems(): Set<String>

    /**
     * Highlights all matches that were found
     *
     * @param highlighter a function that takes any string and highlights it
     */
    fun highlightSearchMatches(highlighter: (String) -> Spanned)

}
