package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class UseCasesTest {

    private val leaf = KuteAction(key = "1", title = "Leaf")
    private val section = KuteSection(key = "2", title = "Section", children = listOf(leaf))
    private val innerCategory = KuteCategory(key = "3", title = "Inner", children = listOf(section))
    private val outerCategory = KuteCategory(key = "4", title = "Outer", children = listOf(innerCategory))
    private val tree = listOf<KutePreferenceListItem>(outerCategory)

    @Test
    fun `finds nested categories`() {
        assertEquals(innerCategory, FindCategoryByKeyUseCase()(tree, innerCategory.key))
    }

    @Test
    fun `unknown category key returns null`() {
        assertNull(FindCategoryByKeyUseCase()(tree, "99"))
    }

    @Test
    fun `item stack is ordered from root to item`() {
        assertEquals(
            listOf(outerCategory, innerCategory, section, leaf),
            FindItemStackUseCase()(tree, leaf),
        )
    }

    @Test
    fun `item stack finds the given instance when keys are shared`() {
        val shared = KuteAction(key = leaf.key, title = "Shares its key with leaf")
        val tree = tree + KuteSection(key = "5", title = "Other", children = listOf(shared))

        assertEquals("5", FindItemStackUseCase()(tree, shared).first().key)
    }

    @Test
    fun `category lookup ignores other items with the same key`() {
        val tree = listOf(KuteAction(key = innerCategory.key, title = "Same key"), outerCategory)

        assertEquals(innerCategory, FindCategoryByKeyUseCase()(tree, innerCategory.key))
    }

    @Test
    fun `item stack throws for an unknown item`() {
        assertThrows(NoSuchElementException::class.java) {
            FindItemStackUseCase()(tree, KuteAction(key = "99", title = "Unknown"))
        }
    }

    @Test
    fun `blank search term has no results`() {
        assertTrue(SearchItemsUseCase()(tree, "  ").isEmpty())
    }

    @Test
    fun `search finds parents as well as their children`() {
        val results = SearchItemsUseCase()(tree, "section leaf").map { it.item }

        assertEquals(listOf<KutePreferenceListItem>(section, leaf), results)
    }

    @Test
    fun `search results have unique keys`() {
        val results = SearchItemsUseCase()(tree, "leaf")

        assertEquals(listOf<KutePreferenceListItem>(leaf), results.map { it.item })
        assertEquals(results.size, results.map { it.key }.distinct().size)
    }

    private val dataProvider = DummyDataProvider()

    private fun text(key: String, defaultValue: String = "", provider: DummyDataProvider = dataProvider) =
        KuteTextPreference(key = key, title = "Text", defaultValue = defaultValue, dataProvider = provider)

    private fun bool(key: String) =
        KuteBooleanPreference(key = key, title = "Bool", defaultValue = true, dataProvider = dataProvider)

    @Test
    fun `items can share a key when they agree on the value`() {
        CheckPreferenceKeysUseCase()(listOf(text("1", "a"), text("1", "a"), text("2", "b")))
    }

    @Test
    fun `sharing a key with a different default value is rejected`() {
        val error = assertThrows(IllegalArgumentException::class.java) {
            CheckPreferenceKeysUseCase()(listOf(text("1", "a"), text("1", "b")))
        }

        assertTrue(error.message!!.contains("\"1\""))
    }

    @Test
    fun `sharing a key with a different value type is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            CheckPreferenceKeysUseCase()(listOf(text("1"), bool("1")))
        }
    }

    @Test
    fun `different data providers do not conflict`() {
        CheckPreferenceKeysUseCase()(listOf(text("1", "a"), text("1", "b", DummyDataProvider())))
    }

    @Test
    fun `the view model checks nested items`() {
        val tree = listOf(KuteSection(key = "9", title = "Section", children = listOf(text("1", "a"), text("1", "b"))))

        assertThrows(IllegalArgumentException::class.java) {
            KutePreferencesViewModel().initPreferencesTree(tree)
        }
    }

}
