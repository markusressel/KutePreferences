package de.markusressel.kutepreferences.core.search

object SearchUtils {

    /**
     * @return true if any of the [String]s in this list contains any _word_ of the [searchTerm], ignoring case
     */
    fun List<String>.containsAnyWord(searchTerm: String): Boolean {
        val words = searchTerm.split("\\s+".toRegex())
        return any { text ->
            words.any { text.contains(it, ignoreCase = true) }
        }
    }

}