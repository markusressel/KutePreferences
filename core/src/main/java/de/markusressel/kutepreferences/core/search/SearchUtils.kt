package de.markusressel.kutepreferences.core.search

object SearchUtils {

    /**
     * @return true if any of the [String]s in this list contains any _word_ of the [searchTerm], ignoring case
     */
    @Deprecated("Provide KutePreferenceListItem.searchContent, which requires all words and ranks the results")
    fun List<String>.containsAnyWord(searchTerm: String): Boolean {
        val words = searchTerm.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
        return any { text ->
            words.any { text.contains(it, ignoreCase = true) }
        }
    }

}