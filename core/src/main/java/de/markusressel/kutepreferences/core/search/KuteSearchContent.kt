package de.markusressel.kutepreferences.core.search

/**
 * The texts of an item that the search looks through, from most to least important.
 *
 * An item matches if each word of the search term is part of any of these texts, ignoring case. A word can also be
 * part of the title of a parent, as long as another word is found in the item itself, so "network proxy" finds the
 * item "Proxy" in the category "Network".
 *
 * Results are ranked by where the words were found. A match at the start of a word in the [title] ranks highest.
 *
 * @param title the title of the item
 * @param keywords terms that are not shown, see
 * [KutePreferenceListItem.keywords][de.markusressel.kutepreferences.core.preference.KutePreferenceListItem.keywords]
 * @param description what the item shows below its title. For a preference this usually is its saved value.
 */
class KuteSearchContent(
    val title: String,
    val keywords: List<String> = emptyList(),
    val description: String = "",
) {

    /**
     * @param parentTitles the titles of the categories and sections that contain the item
     * @return true if the [searchTerm] matches, see [KuteSearchContent]
     */
    fun matches(searchTerm: String, parentTitles: List<String> = emptyList()): Boolean {
        val words = searchTerm.words()
        val ownWords = words.filter { wordScore(it) > NO_MATCH }
        val otherWords = words - ownWords.toSet()
        return ownWords.isNotEmpty() && otherWords.all { word ->
            parentTitles.any { it.contains(word, ignoreCase = true) }
        }
    }

    /**
     * @return how well the [searchTerm] matches this item itself, where a higher value is better. This is
     * [NO_MATCH] if any word is not part of this item, so results that need a parent are listed last.
     */
    fun relevance(searchTerm: String): Int {
        val wordScores = searchTerm.words().map { wordScore(it) }
        return when {
            wordScores.isEmpty() || NO_MATCH in wordScores -> NO_MATCH
            else -> wordScores.sum()
        }
    }

    private fun wordScore(word: String): Int = when {
        title.hasWordStartingWith(word) -> SCORE_TITLE_WORD_START
        title.contains(word, ignoreCase = true) -> SCORE_TITLE
        keywords.any { it.contains(word, ignoreCase = true) } -> SCORE_KEYWORD
        description.contains(word, ignoreCase = true) -> SCORE_DESCRIPTION
        else -> NO_MATCH
    }

    private fun String.words() = trim().split(WHITESPACE).filter { it.isNotEmpty() }

    private fun String.hasWordStartingWith(word: String): Boolean {
        var index = indexOf(word, ignoreCase = true)
        while (index >= 0) {
            if (index == 0 || this[index - 1].isLetterOrDigit().not()) {
                return true
            }
            index = indexOf(word, startIndex = index + 1, ignoreCase = true)
        }
        return false
    }

    companion object {
        const val NO_MATCH = 0

        private const val SCORE_DESCRIPTION = 1
        private const val SCORE_KEYWORD = 2
        private const val SCORE_TITLE = 3
        private const val SCORE_TITLE_WORD_START = 4

        private val WHITESPACE = "\\s+".toRegex()
    }

}
