package de.markusressel.kutepreferences.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.asCondition
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.matches
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ConditionalPreferenceTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val dataProvider = DummyDataProvider()
    private val registry = KuteRegistry(DefaultKuteNavigator())

    private fun show(items: List<KutePreferenceListItem>) {
        composeRule.setContent {
            KutePreferencesTheme {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items.forEach { registry.Content(it) }
                }
            }
        }
        composeRule.waitForIdle()
    }

    private fun action(
        title: String,
        onClick: (() -> Unit)? = null,
        visibleWhen: KuteCondition? = null,
        enabledWhen: KuteCondition? = null,
    ) = KuteAction(key = title, title = title, onClick = onClick, visibleWhen = visibleWhen, enabledWhen = enabledWhen)

    @Test
    fun `an item follows the value of another preference`() {
        val source = KuteBooleanPreference(key = "s", title = "Source", defaultValue = false, dataProvider = dataProvider)
        val dependent = KuteTextPreference(
            key = "d",
            title = "Dependent",
            defaultValue = "",
            dataProvider = dataProvider,
            visibleWhen = source.matches { it },
        )
        show(listOf(source, dependent))
        composeRule.onNodeWithText("Dependent").assertDoesNotExist()

        composeRule.onNodeWithText("Source").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Dependent").assertIsDisplayed()

        composeRule.onNodeWithText("Source").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Dependent").assertDoesNotExist()
    }

    @Test
    fun `a hidden item leaves no gap in a list`() {
        val hidden = MutableStateFlow(false)
        val top = action("Top")
        val middle = action("Middle", visibleWhen = hidden.asCondition())
        val bottom = action("Bottom")
        show(listOf(top, middle, bottom))

        val topPosition = composeRule.onNodeWithText("Top").getBoundsInRoot().top
        val hiddenDistance = composeRule.onNodeWithText("Bottom").getBoundsInRoot().top - topPosition

        hidden.value = true
        composeRule.waitForIdle()
        val shownDistance = composeRule.onNodeWithText("Bottom").getBoundsInRoot().top - topPosition

        // all rows and gaps have the same size, so removing one row and its gap must halve the distance
        assertEquals(shownDistance / 2, hiddenDistance)
    }

    @Test
    fun `an item that starts visible is shown without an animation`() {
        composeRule.mainClock.autoAdvance = false
        show(listOf(action("Visible", visibleWhen = MutableStateFlow(true).asCondition())))
        composeRule.mainClock.advanceTimeByFrame()

        composeRule.onNodeWithText("Visible").assertIsDisplayed()
    }

    @Test
    fun `a disabled item cannot be clicked`() {
        var clicks = 0
        val enabled = MutableStateFlow(false)
        show(listOf(action("Item", onClick = { clicks++ }, enabledWhen = enabled.asCondition())))

        composeRule.onNodeWithContentDescription("Item").performClick()
        composeRule.waitForIdle()
        assertEquals(0, clicks)

        enabled.value = true
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Item").performClick()
        composeRule.waitForIdle()
        assertEquals(1, clicks)
    }

    @Test
    fun `a disabled item offers no accessibility action`() {
        show(listOf(action("Item", enabledWhen = MutableStateFlow(false).asCondition())))

        composeRule.onNodeWithContentDescription("Item").assertIsNotEnabled()
        composeRule.onAllNodes(hasClickAction()).assertCountEquals(0)
    }

    @Test
    fun `conditions also apply to custom item types`() {
        class CustomItem(override val visibleWhen: KuteCondition) : KutePreferenceListItem {
            override val key = "custom"
            override val onClick: (() -> Unit)? = null
            override val onLongClick: (() -> Unit)? = null
            override fun search(searchTerm: String) = false
        }

        val visible = MutableStateFlow(false)
        registry.register<CustomItem> { Text("Custom") }
        show(listOf(CustomItem(visible.asCondition())))
        composeRule.onNodeWithText("Custom").assertDoesNotExist()

        visible.value = true
        composeRule.waitForIdle()

        composeRule.onNodeWithText("Custom").assertIsDisplayed()
    }

}
