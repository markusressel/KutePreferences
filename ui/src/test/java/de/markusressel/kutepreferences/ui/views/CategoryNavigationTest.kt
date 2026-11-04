package de.markusressel.kutepreferences.ui.views

import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.search.KutePreferencesScreen
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import de.markusressel.kutepreferences.ui.vm.KuteUiEvent
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CategoryNavigationTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun fillers(prefix: String) = List(40) { KuteAction(key = "$prefix$it", title = "$prefix $it") }

    private val leaf = KuteCategory(key = "leaf", title = "Leaf category", children = fillers("Leaf"))
    private val nested = KuteCategory(key = "nested", title = "Nested category", children = listOf(leaf))
    private val tree: List<KutePreferenceListItem> = fillers("Top") + nested

    private fun newViewModel() = KutePreferencesViewModel(DefaultKuteNavigator()).apply { initPreferencesTree(tree) }

    private val viewModel = newViewModel()

    private fun showScreen(header: Boolean = true) {
        composeRule.setContent {
            KutePreferencesTheme {
                when {
                    header -> KutePreferencesScreen(
                        modifier = Modifier.height(400.dp),
                        kuteViewModel = viewModel,
                    )

                    else -> KutePreferencesScreen(
                        modifier = Modifier.height(400.dp),
                        kuteViewModel = viewModel,
                        categoryHeader = {},
                    )
                }
            }
        }
        composeRule.waitForIdle()
    }

    private fun openNested() = composeRule.onNodeWithText("Nested category").performScrollTo().performClick()

    private fun pressBack() {
        viewModel.onUiEvent(KuteUiEvent.BackPressed)
        composeRule.waitForIdle()
    }

    @Test
    fun `back returns to the previous scroll position`() {
        showScreen()
        openNested()
        composeRule.onNodeWithText("Leaf category").assertIsDisplayed()

        pressBack()

        composeRule.onNodeWithText("Nested category").assertIsDisplayed()
    }

    @Test
    fun `a category that was left starts at the top the next time`() {
        showScreen()
        openNested()
        composeRule.onNodeWithText("Leaf category").performClick()
        composeRule.onNodeWithText("Leaf 39").performScrollTo()

        pressBack()
        composeRule.onNodeWithText("Leaf category").performClick()

        composeRule.onNodeWithText("Leaf 0").assertIsDisplayed()
    }

    @Test
    fun `the header shows the path and goes back`() {
        showScreen()
        openNested()
        composeRule.onNodeWithText("Leaf category").performClick()

        composeRule.onNodeWithText("Nested category").assertIsDisplayed()
        assertEquals(listOf("nested", "leaf"), viewModel.preferencesUiState.value.categoryPath.map { it.key })

        composeRule.onNodeWithContentDescription("Back").performClick()

        assertEquals(listOf("nested"), viewModel.navigator.categoryStack)
    }

    @Test
    fun `the system back button leaves the category`() {
        lateinit var dispatcher: OnBackPressedDispatcher
        composeRule.setContent {
            dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher
            KutePreferencesTheme {
                KutePreferencesScreen(modifier = Modifier.height(400.dp), kuteViewModel = viewModel)
            }
        }
        composeRule.waitForIdle()
        openNested()
        composeRule.waitForIdle()

        composeRule.runOnUiThread { dispatcher.onBackPressed() }
        composeRule.waitForIdle()

        assertEquals(emptyList<String>(), viewModel.navigator.categoryStack)
    }

    @Test
    fun `a cancelled back gesture stays in the category`() {
        lateinit var dispatcher: OnBackPressedDispatcher
        composeRule.setContent {
            dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher
            KutePreferencesTheme {
                KutePreferencesScreen(modifier = Modifier.height(400.dp), kuteViewModel = viewModel)
            }
        }
        composeRule.waitForIdle()
        openNested()
        composeRule.waitForIdle()

        composeRule.runOnUiThread {
            dispatcher.dispatchOnBackStarted(BackEventCompat(0f, 0f, 0.1f, BackEventCompat.EDGE_LEFT))
            dispatcher.dispatchOnBackProgressed(BackEventCompat(0f, 0f, 0.5f, BackEventCompat.EDGE_LEFT))
            dispatcher.dispatchOnBackCancelled()
        }
        composeRule.waitForIdle()

        assertEquals(listOf("nested"), viewModel.navigator.categoryStack)
        composeRule.onNodeWithText("Leaf category").assertIsDisplayed()
    }

    @Test
    fun `a finished back gesture leaves the category`() {
        lateinit var dispatcher: OnBackPressedDispatcher
        composeRule.setContent {
            dispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher
            KutePreferencesTheme {
                KutePreferencesScreen(modifier = Modifier.height(400.dp), kuteViewModel = viewModel)
            }
        }
        composeRule.waitForIdle()
        openNested()
        composeRule.waitForIdle()

        composeRule.runOnUiThread {
            dispatcher.dispatchOnBackStarted(BackEventCompat(0f, 0f, 0.1f, BackEventCompat.EDGE_LEFT))
            dispatcher.dispatchOnBackProgressed(BackEventCompat(0f, 0f, 0.9f, BackEventCompat.EDGE_LEFT))
            dispatcher.onBackPressed()
        }
        composeRule.waitForIdle()

        assertEquals(emptyList<String>(), viewModel.navigator.categoryStack)
    }

    @Test
    fun `the header can be replaced`() {
        showScreen(header = false)
        openNested()

        composeRule.onNodeWithContentDescription("Back").assertDoesNotExist()
    }

    @Test
    fun `open categories survive the death of the process`() {
        var current by mutableStateOf(viewModel)
        val restoration = StateRestorationTester(composeRule)
        restoration.setContent {
            KutePreferencesTheme {
                KutePreferencesScreen(modifier = Modifier.height(400.dp), kuteViewModel = current)
            }
        }
        composeRule.waitForIdle()
        openNested()
        composeRule.onNodeWithText("Leaf category").performClick()
        composeRule.waitForIdle()

        val restored = newViewModel()
        current = restored
        restoration.emulateSavedInstanceStateRestore()
        composeRule.waitForIdle()

        assertEquals(listOf("nested", "leaf"), restored.navigator.categoryStack)
    }

    @Test
    fun `categories that no longer exist are not restored`() {
        val restoration = StateRestorationTester(composeRule)
        var current by mutableStateOf(viewModel)
        restoration.setContent {
            KutePreferencesTheme {
                KutePreferencesScreen(modifier = Modifier.height(400.dp), kuteViewModel = current)
            }
        }
        composeRule.waitForIdle()
        openNested()
        composeRule.waitForIdle()

        val restored = KutePreferencesViewModel(DefaultKuteNavigator()).apply { initPreferencesTree(fillers("Top")) }
        current = restored
        restoration.emulateSavedInstanceStateRestore()
        composeRule.waitForIdle()

        assertEquals(emptyList<String>(), restored.navigator.categoryStack)
    }

}
