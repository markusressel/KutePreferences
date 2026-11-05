package de.markusressel.kutepreferences.ui.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.search.KutePreferencesScreen
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import de.markusressel.kutepreferences.ui.vm.KuteUiEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class MaxContentWidthTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun show(maxContentWidth: Dp? = null): KutePreferencesViewModel {
        val viewModel = KutePreferencesViewModel(DefaultKuteNavigator())
        viewModel.initPreferencesTree(listOf(KuteAction(key = "item", title = "Item")))
        composeRule.setContent {
            KutePreferencesTheme {
                when (maxContentWidth) {
                    null -> KutePreferencesScreen(modifier = Modifier.fillMaxSize(), kuteViewModel = viewModel)
                    else -> KutePreferencesScreen(
                        modifier = Modifier.fillMaxSize(),
                        kuteViewModel = viewModel,
                        maxContentWidth = maxContentWidth,
                    )
                }
            }
        }
        composeRule.waitForIdle()
        return viewModel
    }

    private fun left(text: String) = composeRule.onNodeWithText(text).getUnclippedBoundsInRoot().left

    // NOTE: the default width of 720dp on a screen of 1000dp leaves a margin of 140dp on each side
    private val margin = 140.dp

    @Test
    @Config(qualifiers = "w1000dp-h600dp")
    fun `the items and the search bar are centered at the maximum width on a large screen`() {
        show()

        assertTrue(left("Item") >= margin)
        assertTrue(left("Search…") >= margin)
    }

    @Test
    @Config(qualifiers = "w1000dp-h600dp")
    fun `the maximum width can be removed`() {
        show(maxContentWidth = Dp.Infinity)

        assertTrue(left("Item") < margin)
        assertTrue(left("Search…") < margin)
    }

    @Test
    @Config(qualifiers = "w400dp-h600dp")
    fun `a small screen uses its full width`() {
        show()

        assertTrue(left("Item") < 100.dp)
    }

    @Test
    @Config(qualifiers = "w1000dp-h600dp")
    fun `search results are centered too`() {
        val viewModel = show()

        viewModel.onUiEvent(KuteUiEvent.SearchFieldSelected)
        viewModel.onUiEvent(KuteUiEvent.SearchTermChanged("item"))
        composeRule.waitForIdle()

        // NOTE: the page below the search is also found by its title, but it has been pushed off the screen
        val results = composeRule.onAllNodesWithText("Item").fetchSemanticsNodes().filter { it.boundsInRoot.width > 0 }
        assertEquals(1, results.size)
        assertTrue(with(composeRule.density) { results.single().boundsInRoot.left.toDp() } >= margin)
    }

}
