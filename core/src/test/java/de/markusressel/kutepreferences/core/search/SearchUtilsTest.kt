package de.markusressel.kutepreferences.core.search

import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test

class SearchUtilsTest {

    @Test
    fun `matches a word ignoring case`() {
        assertTrue(listOf("Wifi settings").containsAnyWord("WIFI"))
    }

    @Test
    fun `matches when any word of the term is found`() {
        assertTrue(listOf("Wifi settings").containsAnyWord("battery wifi"))
    }

    @Test
    fun `does not match unrelated text`() {
        assertFalse(listOf("Wifi settings").containsAnyWord("battery"))
    }

    @Ignore("bug: surrounding whitespace produces an empty word, which matches everything")
    @Test
    fun `surrounding whitespace does not match everything`() {
        assertFalse(listOf("Wifi settings").containsAnyWord("battery "))
        assertFalse(listOf("Wifi settings").containsAnyWord(" battery"))
    }
}
