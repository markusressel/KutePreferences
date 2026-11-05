package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.select.KuteChoicePreference
import de.markusressel.kutepreferences.core.preference.select.KuteMultiChoicePreference
import de.markusressel.kutepreferences.core.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.KuteUrlPreference
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.time.KuteTimePreference
import de.markusressel.kutepreferences.core.preference.time.TimePersistenceModel
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.listitems.TextPreference
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class ValidationMessageTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val dataProvider = DummyDataProvider()
    private val input = hasSetTextAction() and hasAnyAncestor(isDialog())

    private fun open(item: KutePreferenceListItem) {
        val registry = KuteRegistry(DefaultKuteNavigator())
        composeRule.setContent { KutePreferencesTheme { registry.Content(item) } }
        composeRule.onNodeWithText("Title").performClick()
    }

    private fun type(text: String) {
        composeRule.onNode(input).performTextClearance()
        composeRule.onNode(input).performTextInput(text)
    }

    private fun assertShown(message: String) {
        composeRule.onNodeWithText(message).assertIsDisplayed()
        composeRule.onNodeWithText("SAVE").assertIsNotEnabled()
    }

    private fun assertNotShown(message: String) = composeRule.onNodeWithText(message).assertDoesNotExist()

    @Test
    fun `the message of a text follows the input`() {
        open(
            KuteTextPreference(
                key = "t",
                title = "Title",
                defaultValue = "",
                dataProvider = dataProvider,
                validator = { it.length <= 2 },
                validationMessage = { "${it.length} is too long" },
            )
        )
        assertNotShown("3 is too long")

        type("abc")
        assertShown("3 is too long")

        type("ab")
        assertNotShown("3 is too long")
    }

    @Test
    fun `a password explains its minimum length`() {
        open(KutePasswordPreference(key = "p", title = "Title", defaultValue = "", dataProvider = dataProvider))

        type("short")

        assertShown("Must have at least 8 characters")
    }

    @Test
    fun `a url explains that it is invalid`() {
        open(KuteUrlPreference(key = "u", title = "Title", defaultValue = "https://example.com", dataProvider = dataProvider))
        assertNotShown("Enter a valid URL")

        type("nope")

        assertShown("Enter a valid URL")
    }

    @Test
    fun `a number explains its range`() {
        open(
            KuteNumberPreference(
                key = "n",
                title = "Title",
                minimum = 1,
                maximum = 9,
                defaultValue = 5,
                dataProvider = dataProvider,
            )
        )
        assertNotShown("Must be between 1 and 9")

        type("10")

        assertShown("Must be between 1 and 9")
    }

    @Test
    fun `a number with one limit explains it`() {
        open(KuteNumberPreference(key = "n", title = "Title", maximum = 9, defaultValue = 5, dataProvider = dataProvider))

        type("10")

        assertShown("Must be at most 9")
    }

    @Test
    fun `a cleared number asks for a number`() {
        open(KuteNumberPreference(key = "n", title = "Title", defaultValue = 5, dataProvider = dataProvider))

        composeRule.onNode(input).performTextClearance()

        assertShown("Enter a number")
    }

    @Test
    fun `a validator that throws shows the generic text`() {
        open(
            KuteTextPreference(
                key = "t",
                title = "Title",
                defaultValue = "",
                dataProvider = dataProvider,
                validator = { error("broken") },
            )
        )

        assertShown("This value is not allowed")
    }

    @Test
    fun `a behavior that accepts a value with a custom isValid shows no message`() {
        val behavior = object : TextPreferenceBehavior(
            KuteTextPreference(
                key = "t",
                title = "Title",
                regex = "a+",
                defaultValue = "b",
                dataProvider = dataProvider,
            )
        ) {
            override fun isValid() = true
        }
        composeRule.setContent { KutePreferencesTheme { TextPreference(behavior) } }
        composeRule.onNodeWithText("Title").performClick()

        composeRule.onNodeWithText("SAVE").assertIsEnabled()
        assertNotShown("This value is not allowed")
    }

    @Test
    fun `a choice shows its message`() {
        open(
            KuteChoicePreference(
                key = "c",
                title = "Title",
                defaultValue = "a",
                options = mapOf("a" to "First"),
                dataProvider = dataProvider,
                validator = { false },
                validationMessage = { "Not this one" },
            )
        )

        assertShown("Not this one")
    }

    @Test
    fun `a multi choice shows its message`() {
        open(
            KuteMultiChoicePreference(
                key = "m",
                title = "Title",
                options = mapOf("a" to "First"),
                dataProvider = dataProvider,
                validator = { it.isNotEmpty() },
                validationMessage = { "Select at least one" },
            )
        )

        assertShown("Select at least one")
    }

    @Test
    @Config(sdk = [34], qualifiers = "w411dp-h891dp")
    fun `a time shows its message`() {
        open(
            KuteTimePreference(
                key = "h",
                title = "Title",
                defaultValue = TimePersistenceModel(12, 0),
                dataProvider = dataProvider,
                validator = { false },
                validationMessage = { "Not at noon" },
            )
        )

        assertShown("Not at noon")
    }

    @Test
    @Config(sdk = [34], qualifiers = "w411dp-h891dp")
    fun `a date shows its message`() {
        open(
            KuteDatePreference(
                key = "d",
                title = "Title",
                defaultValue = 0,
                dataProvider = dataProvider,
                validator = { false },
                validationMessage = { "Not this day" },
            )
        )

        assertShown("Not this day")
    }

}
