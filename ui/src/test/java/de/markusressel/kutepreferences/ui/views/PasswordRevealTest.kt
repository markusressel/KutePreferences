package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextLayoutResult
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.PasswordPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.listitems.PasswordPreference
import de.markusressel.kutepreferences.ui.views.listitems.TextPreference
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PasswordRevealTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val password = "secret123"

    private fun openDialog() {
        val behavior = PasswordPreferenceBehavior(
            KutePasswordPreference(
                key = "password",
                title = "Password",
                defaultValue = password,
                dataProvider = DummyDataProvider(),
            )
        )
        composeRule.setContent { KutePreferencesTheme { PasswordPreference(behavior) } }
        composeRule.onNodeWithText("Password").performClick()
    }

    private fun show() = composeRule.onNodeWithContentDescription("Show password")
    private fun hide() = composeRule.onNodeWithContentDescription("Hide password")

    /**
     * The text that is drawn. The semantics of a text field always contain the raw value, even when it is masked.
     */
    private fun drawnText(): String {
        val results = mutableListOf<TextLayoutResult>()
        val node = composeRule.onNode(hasSetTextAction()).fetchSemanticsNode()
        node.config[SemanticsActions.GetTextLayoutResult].action!!(results)
        return results.single().layoutInput.text.text
    }

    private fun assertRevealed() = assertEquals(password, drawnText())

    private fun assertHidden() {
        assertNotEquals(password, drawnText())
        assertEquals(password.length, drawnText().length)
    }

    @Test
    fun `the password is hidden when the dialog opens`() {
        openDialog()

        assertHidden()
        show().assertIsDisplayed()
    }

    @Test
    fun `the button reveals and hides the password`() {
        openDialog()

        show().performClick()
        assertRevealed()

        hide().performClick()
        assertHidden()
    }

    @Test
    fun `the password is hidden again when the dialog is reopened`() {
        openDialog()
        show().performClick()
        assertRevealed()

        composeRule.onNodeWithText("CANCEL").performClick()
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Password").performClick()

        assertHidden()
        show().assertIsDisplayed()
    }

    @Test
    fun `a text preference has no button`() {
        val behavior = TextPreferenceBehavior(
            KuteTextPreference(
                key = "text",
                title = "Text",
                defaultValue = "",
                dataProvider = DummyDataProvider(),
            )
        )
        composeRule.setContent { KutePreferencesTheme { TextPreference(behavior) } }
        composeRule.onNodeWithText("Text").performClick()

        show().assertDoesNotExist()
    }

}
