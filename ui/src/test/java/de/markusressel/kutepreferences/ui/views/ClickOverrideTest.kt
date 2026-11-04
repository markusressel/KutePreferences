package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.bool.BooleanPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.listitems.BooleanPreference
import de.markusressel.kutepreferences.ui.views.listitems.TextPreference
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ClickOverrideTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val dataProvider = DummyDataProvider()

    private fun booleanBehavior(onClick: (() -> Unit)? = null, onLongClick: (() -> Unit)? = null) =
        BooleanPreferenceBehavior(
            KuteBooleanPreference(
                key = "1",
                title = "Toggle",
                defaultValue = true,
                dataProvider = dataProvider,
                onClick = onClick,
                onLongClick = onLongClick,
            )
        )

    @Test
    fun `click toggles a boolean without an override`() {
        val behavior = booleanBehavior()
        composeRule.setContent { KutePreferencesTheme { BooleanPreference(behavior) } }

        composeRule.onNodeWithText("Toggle").performClick()

        composeRule.runOnIdle { assertFalse(behavior.persistedValue.value) }
    }

    @Test
    fun `click override replaces the default action`() {
        var clicks = 0
        val behavior = booleanBehavior(onClick = { clicks++ })
        composeRule.setContent { KutePreferencesTheme { BooleanPreference(behavior) } }

        composeRule.onNodeWithText("Toggle").performClick()

        composeRule.runOnIdle {
            assertEquals(1, clicks)
            assertEquals(true, behavior.persistedValue.value)
        }
    }

    @Test
    fun `long click override is invoked`() {
        var longClicks = 0
        val behavior = booleanBehavior(onLongClick = { longClicks++ })
        composeRule.setContent { KutePreferencesTheme { BooleanPreference(behavior) } }

        composeRule.onNodeWithText("Toggle").performTouchInput { longClick() }

        composeRule.runOnIdle { assertEquals(1, longClicks) }
    }

    @Test
    fun `click override on a text preference does not open the dialog`() {
        var clicks = 0
        val behavior = TextPreferenceBehavior(
            KuteTextPreference(
                key = "2",
                title = "Text",
                defaultValue = "value",
                dataProvider = dataProvider,
                onClick = { clicks++ },
            )
        )
        composeRule.setContent { KutePreferencesTheme { TextPreference(behavior) } }

        composeRule.onNodeWithText("Text").performClick()

        composeRule.runOnIdle { assertEquals(1, clicks) }
    }
}
