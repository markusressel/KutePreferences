package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.NumberPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.listitems.NumberPreference
import de.markusressel.kutepreferences.ui.views.listitems.TextPreference
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DialogValidationTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val dataProvider = DummyDataProvider()

    private val input = hasSetTextAction() and hasAnyAncestor(isDialog())

    private fun save() = composeRule.onNodeWithText("SAVE")

    @Test
    fun `save is disabled while the validator rejects the input`() {
        val behavior = TextPreferenceBehavior(
            KuteTextPreference(
                key = "1",
                title = "Name",
                defaultValue = "ok",
                dataProvider = dataProvider,
                validator = { it.length <= 2 },
            )
        )
        composeRule.setContent { KutePreferencesTheme { TextPreference(behavior) } }
        composeRule.onNodeWithText("Name").performClick()

        composeRule.onNode(input).performTextClearance()
        composeRule.onNode(input).performTextInput("too long")
        save().assertIsNotEnabled()

        composeRule.onNode(input).performTextClearance()
        composeRule.onNode(input).performTextInput("ab")
        save().assertIsEnabled().performClick()

        composeRule.runOnIdle { assertEquals("ab", behavior.persistedValue.value) }
    }

    @Test
    fun `a cleared number cannot be saved`() {
        val behavior = NumberPreferenceBehavior(
            KuteNumberPreference(
                key = "2",
                title = "Count",
                defaultValue = 5,
                dataProvider = dataProvider,
            )
        )
        composeRule.setContent { KutePreferencesTheme { NumberPreference(behavior) } }
        composeRule.onNodeWithText("Count").performClick()

        composeRule.onNode(input).performTextClearance()
        save().assertIsNotEnabled()

        composeRule.onNode(input).performTextInput("7")
        save().assertIsEnabled().performClick()

        composeRule.runOnIdle { assertEquals(7L, behavior.persistedValue.value) }
    }

}
