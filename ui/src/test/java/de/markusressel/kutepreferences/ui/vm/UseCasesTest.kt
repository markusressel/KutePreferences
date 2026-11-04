package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class UseCasesTest {

    private val leaf = KuteAction(key = 1, title = "Leaf")
    private val section = KuteSection(key = 2, title = "Section", children = listOf(leaf))
    private val innerCategory = KuteCategory(key = 3, title = "Inner", children = listOf(section))
    private val outerCategory = KuteCategory(key = 4, title = "Outer", children = listOf(innerCategory))
    private val tree = listOf<KutePreferenceListItem>(outerCategory)

    @Test
    fun `finds nested categories`() {
        assertEquals(innerCategory, FindCategoryByKeyUseCase()(tree, innerCategory.key))
    }

    @Test
    fun `unknown category key returns null`() {
        assertNull(FindCategoryByKeyUseCase()(tree, 99))
    }

    @Test
    fun `item stack is ordered from root to item`() {
        assertEquals(
            listOf(outerCategory, innerCategory, section, leaf),
            FindItemStackUseCase()(tree, leaf),
        )
    }

    @Test
    fun `item stack throws for an unknown item`() {
        assertThrows(NoSuchElementException::class.java) {
            FindItemStackUseCase()(tree, KuteAction(key = 99, title = "Unknown"))
        }
    }

    @Test
    fun `blank search term has no results`() {
        assertTrue(SearchItemsUseCase()(tree, "  ").isEmpty())
    }

    @Test
    fun `search results have unique keys`() {
        val results = SearchItemsUseCase()(tree, "leaf")

        assertEquals(listOf<KutePreferenceListItem>(leaf), results.map { it.item })
        assertEquals(results.size, results.map { it.key }.distinct().size)
    }
}
