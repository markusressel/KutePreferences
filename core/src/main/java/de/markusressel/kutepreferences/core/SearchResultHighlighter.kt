package de.markusressel.kutepreferences.core

import android.text.Spanned

typealias HighlighterFunction = (String) -> Spanned

interface SearchResultHighlighter {

    /**
     * Highlights all matches that were found
     *
     * @param text the text to highlight
     * @param highlighter a function that takes any string and highlights it
     * @return the highlighted text
     */
    fun highlightSearchMatch(text: String, highlighter: HighlighterFunction): Spanned {
        return highlighter.invoke(text)
    }

}