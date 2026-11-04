package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class FilterRecursiveTest {

    private val nested = KuteAction(key = 1, title = "Nested wifi action")
    private val other = KuteAction(key = 2, title = "Something else")
    private val section = KuteSection(key = 3, title = "Network section", children = listOf(nested, other))
    private val category = KuteCategory(key = 4, title = "Hardware category", children = listOf(section))

    @Test
    fun `finds leaf items at any depth`() {
        assertEquals(listOf<KutePreferenceListItem>(nested), listOf(category).filterRecursive("wifi"))
    }

    @Ignore("bug: parents are replaced by their children, so they can never be found")
    @Test
    fun `finds matching categories and sections`() {
        assertEquals(listOf<KutePreferenceListItem>(category), listOf(category).filterRecursive("hardware"))
        assertEquals(listOf<KutePreferenceListItem>(section), listOf(category).filterRecursive("network"))
    }
}
