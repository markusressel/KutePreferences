package de.markusressel.kutepreferences.core.search

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KuteSearchContentTest {

    private val content = KuteSearchContent(
        title = "Dark mode",
        keywords = listOf("night", "theme"),
        description = "Follows the system",
    )

    @Test
    fun `all words have to match`() {
        assertTrue(content.matches("dark mode"))
        assertFalse(content.matches("dark battery"))
        assertFalse(content.matches("battery mode"))
    }

    @Test
    fun `words can match in different texts in any order and case`() {
        assertTrue(content.matches("SYSTEM night DARK"))
    }

    @Test
    fun `a part of a word matches`() {
        assertTrue(content.matches("ark"))
    }

    @Test
    fun `a blank term does not match`() {
        assertFalse(content.matches(""))
        assertFalse(content.matches("   "))
        assertEquals(KuteSearchContent.NO_MATCH, content.relevance("   "))
    }

    @Test
    fun `surrounding whitespace is ignored`() {
        assertTrue(content.matches("  dark   mode "))
    }

    @Test
    fun `the title of a parent can complete a match`() {
        assertTrue(content.matches("display dark", parentTitles = listOf("Display")))
    }

    @Test
    fun `the title of a parent cannot match on its own`() {
        assertFalse(content.matches("display", parentTitles = listOf("Display")))
    }

    @Test
    fun `a word in neither the item nor a parent prevents a match`() {
        assertFalse(content.matches("display dark battery", parentTitles = listOf("Display")))
    }

    @Test
    fun `relevance prefers the start of a title word, the title, keywords and then the description`() {
        val scores = listOf("mode", "ode", "night", "system").map { content.relevance(it) }

        assertEquals(scores.sortedDescending(), scores)
        assertEquals(scores.size, scores.distinct().size)
    }

    @Test
    fun `relevance counts every word`() {
        assertTrue(content.relevance("dark mode") > content.relevance("dark"))
    }

    @Test
    fun `the start of a later occurrence counts as a word start`() {
        val repeated = KuteSearchContent(title = "Superwifi wifi")

        assertEquals(KuteSearchContent(title = "Wifi").relevance("wifi"), repeated.relevance("wifi"))
    }

    @Test
    fun `relevance does not count parents`() {
        assertEquals(KuteSearchContent.NO_MATCH, content.relevance("display dark"))
    }

}
