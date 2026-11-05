package de.markusressel.kutepreferences.ui.views

import android.os.Looper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.search.KutePreferencesScreen
import de.markusressel.kutepreferences.ui.views.search.TwoPaneLayout
import de.markusressel.kutepreferences.ui.views.search.WindowHinge
import de.markusressel.kutepreferences.ui.views.search.twoPaneLayout
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.time.Duration

@RunWith(RobolectricTestRunner::class)
class TwoPaneLayoutTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val nested = KuteCategory(
        key = "nested",
        title = "Nested",
        children = listOf(KuteAction(key = "deep", title = "Deep item")),
    )
    private val network = KuteCategory(
        key = "network",
        title = "Network",
        children = listOf(KuteAction(key = "wifi", title = "Wifi item"), nested),
    )
    private val display = KuteCategory(
        key = "display",
        title = "Display",
        children = listOf(KuteAction(key = "dark", title = "Dark item")),
    )
    private val tree = listOf<KutePreferenceListItem>(network, display, KuteAction(key = "about", title = "About"))

    private val navigator = DefaultKuteNavigator()

    private fun show(
        items: List<KutePreferenceListItem> = tree,
        twoPaneMinWidth: Dp? = null,
    ): KutePreferencesViewModel {
        val viewModel = KutePreferencesViewModel(navigator)
        viewModel.initPreferencesTree(items)
        composeRule.setContent {
            KutePreferencesTheme {
                when (twoPaneMinWidth) {
                    null -> KutePreferencesScreen(modifier = Modifier.fillMaxSize(), kuteViewModel = viewModel)
                    else -> KutePreferencesScreen(
                        modifier = Modifier.fillMaxSize(),
                        kuteViewModel = viewModel,
                        twoPaneMinWidth = twoPaneMinWidth,
                    )
                }
            }
        }
        composeRule.waitForIdle()
        return viewModel
    }

    private fun exists(text: String) = composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()

    private fun left(text: String) = composeRule.onNodeWithText(text).getUnclippedBoundsInRoot().left

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `a wide screen shows the top level with a hint when no category is open`() {
        show()

        composeRule.onNodeWithText("Network").assertIsDisplayed()
        composeRule.onNodeWithText("About").assertIsDisplayed()
        composeRule.onNodeWithText("Select a category").assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `a selected category opens next to the top level`() {
        show()

        composeRule.onNodeWithText("Network").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Display").assertIsDisplayed()
        composeRule.onNodeWithText("Wifi item").assertIsDisplayed()
        assertTrue(left("Wifi item") >= 360.dp)
        assertTrue(left("About") < 360.dp)
        assertTrue(exists("Select a category").not())
    }

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `only the open category is marked as selected`() {
        show()

        composeRule.onNodeWithText("Network").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Display").assertIsNotSelected()
        // NOTE: the second one is the header of the detail pane
        composeRule.onAllNodesWithText("Network")[0].assertIsSelected()
    }

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `selecting another category replaces the open one, so back does not return to it`() {
        show()

        composeRule.onNodeWithText("Network").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Display").performClick()
        composeRule.waitForIdle()

        assertEquals(listOf("display"), navigator.categoryStack)
        composeRule.onNodeWithText("Dark item").assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `a nested category opens in the detail pane and keeps its parent selected`() {
        show()

        composeRule.onNodeWithText("Network").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Nested").performClick()
        composeRule.waitForIdle()

        assertEquals(listOf("network", "nested"), navigator.categoryStack)
        composeRule.onNodeWithText("Deep item").assertIsDisplayed()
        composeRule.onNodeWithText("Display").assertIsDisplayed()
        composeRule.onAllNodesWithText("Network")[0].assertIsSelected()
    }

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `the back button of the header closes the open category`() {
        show()

        composeRule.onNodeWithText("Display").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.waitForIdle()

        assertTrue(navigator.categoryStack.isEmpty())
        composeRule.onNodeWithText("Select a category").assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `a click override of a category still replaces opening it`() {
        var clicked = false
        val custom = KuteCategory(key = "custom", title = "Custom", onClick = { clicked = true })
        show(items = listOf(custom))

        composeRule.onNodeWithText("Custom").performClick()
        composeRule.waitForIdle()

        assertTrue(clicked)
        assertTrue(navigator.categoryStack.isEmpty())
    }

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `a category inside of a section also counts`() {
        show(items = listOf(KuteSection(key = "s", title = "Section", children = listOf(display))))

        composeRule.onNodeWithText("Select a category").assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `preferences without categories stay a single page`() {
        show(items = listOf(KuteAction(key = "about", title = "About")))

        assertTrue(exists("Select a category").not())
    }

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `navigating to an item opens its category in the detail pane`() {
        val viewModel = show()

        viewModel.navigateTo("wifi")
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(1))
        composeRule.waitForIdle()

        assertEquals(listOf("network"), navigator.categoryStack)
        composeRule.onNodeWithText("Wifi item").assertIsDisplayed()
        assertTrue(left("Wifi item") >= 360.dp)
    }

    @Test
    @Config(qualifiers = "w1000dp-h700dp")
    fun `the two pane layout can be turned off`() {
        show(twoPaneMinWidth = Dp.Infinity)

        assertTrue(exists("Select a category").not())
        composeRule.onNodeWithText("Network").performClick()
        composeRule.waitForIdle()
        assertTrue(exists("Display").not())
    }

    @Test
    @Config(qualifiers = "w600dp-h900dp")
    fun `a screen below the minimum width shows a single page`() {
        show()

        assertTrue(exists("Select a category").not())
        composeRule.onNodeWithText("Network").performClick()
        composeRule.waitForIdle()
        assertTrue(exists("Display").not())
        composeRule.onNodeWithText("Wifi item").assertIsDisplayed()
    }

    @Test
    fun `the panes only appear from the minimum width on`() {
        assertNull(twoPaneLayout(width = 839.dp, twoPaneMinWidth = 840.dp, hinge = null))
        assertEquals(TwoPaneLayout(360.dp, 0.dp), twoPaneLayout(width = 840.dp, twoPaneMinWidth = 840.dp, hinge = null))
    }

    @Test
    fun `a hinge divides the panes, even below the minimum width`() {
        assertEquals(
            TwoPaneLayout(listWidth = 340.dp, gap = 20.dp),
            twoPaneLayout(width = 700.dp, twoPaneMinWidth = 840.dp, hinge = 340.dp..360.dp),
        )
    }

    @Test
    fun `a hinge that leaves too little space for a pane is ignored`() {
        assertNull(twoPaneLayout(width = 700.dp, twoPaneMinWidth = 840.dp, hinge = 100.dp..120.dp))
        assertNull(twoPaneLayout(width = 700.dp, twoPaneMinWidth = 840.dp, hinge = 500.dp..520.dp))
        assertEquals(
            TwoPaneLayout(360.dp, 0.dp),
            twoPaneLayout(width = 900.dp, twoPaneMinWidth = 840.dp, hinge = 100.dp..120.dp),
        )
    }

    @Test
    fun `turning the panes off also ignores a hinge`() {
        assertNull(twoPaneLayout(width = 700.dp, twoPaneMinWidth = Dp.Infinity, hinge = 340.dp..360.dp))
    }

    @Test
    fun `a hinge is converted to the start of the layout`() {
        val hinge = WindowHinge(leftPx = 500f, rightPx = 520f)

        assertEquals(400f..420f, hinge.relativeTo(100f, 800f, LayoutDirection.Ltr))
        assertEquals(380f..400f, hinge.relativeTo(100f, 800f, LayoutDirection.Rtl))
        assertNull(hinge.relativeTo(600f, 400f, LayoutDirection.Ltr))
        assertNull(hinge.relativeTo(0f, 400f, LayoutDirection.Ltr))
    }

}
