package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KutePreferencesViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val navigator = DefaultKuteNavigator()

    private val leaf = KuteAction(key = "1", title = "Nested leaf")
    private val category = KuteCategory(key = "2", title = "Category", children = listOf(leaf))
    private val topLevel = KuteAction(key = "3", title = "Top level")
    private val tree = listOf<KutePreferenceListItem>(category, topLevel)

    private lateinit var viewModel: KutePreferencesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = KutePreferencesViewModel(navigator)
        viewModel.initPreferencesTree(tree)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val state get() = viewModel.preferencesUiState.value

    @Test
    fun `shows the top level items initially`() = runTest(dispatcher) {
        advanceUntilIdle()

        assertEquals(tree, state.preferenceItems)
        assertNull(state.currentCategory)
    }

    @Test
    fun `entering a category shows its children`() = runTest(dispatcher) {
        navigator.enterCategory(category.key)
        advanceUntilIdle()

        assertEquals(category.key, state.currentCategory)
        assertEquals(listOf<KutePreferenceListItem>(leaf), state.preferenceItems)
    }

    @Test
    fun `back leaves the category`() = runTest(dispatcher) {
        navigator.enterCategory(category.key)
        advanceUntilIdle()

        viewModel.onUiEvent(KuteUiEvent.BackPressed)
        advanceUntilIdle()

        assertNull(state.currentCategory)
    }

    @Test
    fun `search term produces results and back closes the search`() = runTest(dispatcher) {
        advanceUntilIdle()

        viewModel.onUiEvent(KuteUiEvent.SearchFieldSelected)
        viewModel.onUiEvent(KuteUiEvent.SearchTermChanged("leaf"))
        advanceUntilIdle()

        assertTrue(state.isSearchActive)
        assertEquals(listOf<KutePreferenceListItem>(leaf), state.searchResults.map { it.item })

        viewModel.onUiEvent(KuteUiEvent.BackPressed)
        advanceUntilIdle()

        assertFalse(state.isSearchActive)
        assertEquals("", state.searchTerm)
    }

    @Test
    fun `selecting a search result navigates to its category`() = runTest(dispatcher) {
        advanceUntilIdle()

        viewModel.onUiEvent(KuteUiEvent.SearchFieldSelected)
        viewModel.onUiEvent(KuteUiEvent.SearchTermChanged("leaf"))
        advanceUntilIdle()

        viewModel.onUiEvent(KuteUiEvent.SearchResultSelected(state.searchResults.single()))
        advanceUntilIdle()

        assertFalse(state.isSearchActive)
        assertEquals(category.key, state.currentCategory)
        assertEquals(listOf<KutePreferenceListItem>(leaf), state.preferenceItems)
    }
}
