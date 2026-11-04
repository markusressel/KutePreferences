package de.markusressel.kutepreferences.ui.views

import android.R
import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.select.KuteChoicePreference
import de.markusressel.kutepreferences.core.preference.select.KuteMultiChoicePreference
import de.markusressel.kutepreferences.core.preference.select.KuteMultiSelectStringPreference
import de.markusressel.kutepreferences.core.preference.select.KuteSingleSelectStringPreference
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SelectionDialogTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val dataProvider = DummyDataProvider()
    private val registry = KuteRegistry(DefaultKuteNavigator())

    private fun show(item: KutePreferenceListItem, title: String) {
        composeRule.setContent { KutePreferencesTheme { registry.Content(item) } }
        composeRule.onNodeWithText(title).performClick()
        composeRule.waitForIdle()
    }

    private fun clickAndSave(option: String) {
        composeRule.onNodeWithText(option).performClick()
        composeRule.onNodeWithText("SAVE").performClick()
        composeRule.waitForIdle()
    }

    @Test
    fun `a choice saves the selected option`() {
        val item = KuteChoicePreference(
            key = "c",
            title = "Choice",
            defaultValue = "a",
            options = mapOf("a" to "First", "b" to "Second"),
            dataProvider = dataProvider,
        )
        show(item, "Choice")

        clickAndSave("Second")

        composeRule.runOnIdle { assertEquals("b", item.getCurrentPersistedValue()) }
    }

    @Test
    fun `a multi choice toggles the selected options`() {
        val item = KuteMultiChoicePreference(
            key = "m",
            title = "Multi",
            defaultValue = setOf("a"),
            options = mapOf("a" to "First", "b" to "Second"),
            dataProvider = dataProvider,
        )
        show(item, "Multi")

        // the title of the selected option is also shown as the description of the list item
        composeRule.onAllNodesWithText("First").onLast().performClick()
        clickAndSave("Second")

        composeRule.runOnIdle { assertEquals(setOf("b"), item.getCurrentPersistedValue()) }
    }

    @Test
    fun `items with string resources still save the resolved key`() {
        val item = KuteSingleSelectStringPreference(
            context = context,
            key = "s",
            title = "Resources",
            defaultValue = R.string.copy,
            possibleValues = mapOf(R.string.copy to R.string.paste, R.string.cut to R.string.selectAll),
            dataProvider = dataProvider,
        )
        show(item, "Resources")

        clickAndSave(context.getString(R.string.selectAll))

        composeRule.runOnIdle { assertEquals(context.getString(R.string.cut), item.getCurrentPersistedValue()) }
    }

    @Test
    fun `multi items with string resources still save the resolved keys`() {
        val item = KuteMultiSelectStringPreference(
            context = context,
            key = "s",
            title = "Resources",
            defaultValue = setOf(R.string.copy),
            possibleValues = mapOf(R.string.copy to R.string.paste, R.string.cut to R.string.selectAll),
            dataProvider = dataProvider,
        )
        show(item, "Resources")

        clickAndSave(context.getString(R.string.selectAll))

        composeRule.runOnIdle {
            assertEquals(
                setOf(context.getString(R.string.copy), context.getString(R.string.cut)),
                item.getCurrentPersistedValue(),
            )
        }
    }

}
