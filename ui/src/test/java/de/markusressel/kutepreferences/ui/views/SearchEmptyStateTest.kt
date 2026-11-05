package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.search.KutePreferencesScreen
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import de.markusressel.kutepreferences.ui.vm.KuteUiEvent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchEmptyStateTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val viewModel = KutePreferencesViewModel(DefaultKuteNavigator()).apply {
        initPreferencesTree(listOf(KuteAction(key = "wifi", title = "Wifi")))
    }

    @Before
    fun openSearch() {
        composeRule.setContent { KutePreferencesTheme { KutePreferencesScreen(kuteViewModel = viewModel) } }
        composeRule.waitForIdle()
        viewModel.onUiEvent(KuteUiEvent.SearchFieldSelected)
        composeRule.waitForIdle()
    }

    private fun search(term: String) {
        viewModel.onUiEvent(KuteUiEvent.SearchTermChanged(term))
        composeRule.waitForIdle()
    }

    private fun noResults(term: String) = composeRule.onNodeWithText("No results for \"$term\"")

    @Test
    fun `a search without results says so`() {
        search("bluetooth")

        noResults("bluetooth").assertIsDisplayed()
    }

    @Test
    fun `the message follows the search term`() {
        search("bluetooth")
        search("nfc")

        noResults("bluetooth").assertDoesNotExist()
        noResults("nfc").assertIsDisplayed()
    }

    @Test
    fun `a search with results shows no message`() {
        search("wifi")

        noResults("wifi").assertDoesNotExist()
    }

    @Test
    fun `an empty search shows no message`() {
        noResults("").assertDoesNotExist()

        search("   ")

        noResults("   ").assertDoesNotExist()
    }

    @Test
    fun `clearing the search removes the message`() {
        search("bluetooth")
        search("")

        noResults("bluetooth").assertDoesNotExist()
        noResults("").assertDoesNotExist()
    }

}
