package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.bool.KuteMainSwitchPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.search.KutePreferencesScreen
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import de.markusressel.kutepreferences.ui.vm.KuteUiEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w411dp-h891dp")
class MainSwitchTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val dataProvider = DummyDataProvider()

    private fun switch(key: String, title: String, defaultValue: Boolean = true) =
        KuteBooleanPreference(key = key, title = title, defaultValue = defaultValue, dataProvider = dataProvider)

    private fun mainSwitch(defaultValue: Boolean, key: String = "main", title: String = "Main") =
        KuteMainSwitchPreference(key = key, title = title, defaultValue = defaultValue, dataProvider = dataProvider)

    private fun show(items: List<KutePreferenceListItem>): KutePreferencesViewModel {
        val viewModel = KutePreferencesViewModel(DefaultKuteNavigator())
        viewModel.initPreferencesTree(items)
        composeRule.setContent { KutePreferencesTheme { KutePreferencesScreen(kuteViewModel = viewModel) } }
        composeRule.waitForIdle()
        return viewModel
    }

    /**
     * A disabled item replaces its semantics, and it is then only found by its title as the content description
     */
    private fun assertDisabled(title: String) {
        composeRule.onNodeWithContentDescription(title).assertIsNotEnabled()
    }

    private fun assertNotDisabled(title: String) {
        assertEquals(0, composeRule.onAllNodesWithContentDescription(title).fetchSemanticsNodes().size)
        composeRule.onNodeWithText(title).assertExists()
    }

    @Test
    fun `other items are enabled while the main switch is on`() {
        show(listOf(mainSwitch(defaultValue = true), switch("a", "Dependent")))

        assertNotDisabled("Dependent")
    }

    @Test
    fun `other items are disabled while the main switch is off`() {
        show(listOf(mainSwitch(defaultValue = false), switch("a", "Dependent")))

        assertDisabled("Dependent")
    }

    @Test
    fun `the main switch stays usable while it is off`() {
        val main = mainSwitch(defaultValue = false)
        show(listOf(main, switch("a", "Dependent")))

        composeRule.onNodeWithText("Main").performClick()
        composeRule.waitForIdle()

        assertTrue(main.persistedValue.value)
        assertNotDisabled("Dependent")
    }

    @Test
    fun `turning the main switch off disables the items and keeps their values`() {
        val main = mainSwitch(defaultValue = true)
        val dependent = switch("a", "Dependent", defaultValue = true)
        show(listOf(main, dependent))

        composeRule.onNodeWithText("Main").performClick()
        composeRule.waitForIdle()

        assertFalse(main.persistedValue.value)
        assertDisabled("Dependent")
        assertTrue(dependent.persistedValue.value)
    }

    @Test
    fun `a disabled item ignores clicks`() {
        val dependent = switch("a", "Dependent", defaultValue = true)
        show(listOf(mainSwitch(defaultValue = false), dependent))

        composeRule.onNodeWithContentDescription("Dependent").performClick()
        composeRule.waitForIdle()

        assertTrue(dependent.persistedValue.value)
    }

    @Test
    fun `items outside of the parent of the main switch are not affected`() {
        show(
            listOf(
                KuteSection(key = "s", title = "Section", children = listOf(mainSwitch(defaultValue = false), switch("a", "Inside"))),
                switch("b", "Outside"),
            ),
        )

        assertDisabled("Inside")
        assertNotDisabled("Outside")
    }

    @Test
    fun `items in a disabled search result stay disabled`() {
        val viewModel = show(
            listOf(
                KuteCategory(
                    key = "c",
                    title = "Category",
                    children = listOf(
                        mainSwitch(defaultValue = false),
                        KuteSection(key = "s", title = "Section", children = listOf(switch("a", "Nested"))),
                    ),
                ),
            ),
        )

        viewModel.onUiEvent(KuteUiEvent.SearchFieldSelected)
        viewModel.onUiEvent(KuteUiEvent.SearchTermChanged("nested"))
        composeRule.waitForIdle()

        assertDisabled("Nested")
    }

    @Test
    fun `a nested main switch only applies while its parent one is on`() {
        val outer = mainSwitch(defaultValue = false, key = "outer", title = "Outer")
        val viewModel = show(
            listOf(
                outer,
                KuteSection(
                    key = "s",
                    title = "Section",
                    children = listOf(mainSwitch(defaultValue = true, key = "inner", title = "Inner"), switch("a", "Leaf")),
                ),
            ),
        )

        viewModel.onUiEvent(KuteUiEvent.SearchFieldSelected)
        viewModel.onUiEvent(KuteUiEvent.SearchTermChanged("leaf"))
        composeRule.waitForIdle()
        assertDisabled("Leaf")

        viewModel.onUiEvent(KuteUiEvent.CloseSearch)
        composeRule.waitForIdle()
        composeRule.onNodeWithText("Outer").performClick()
        composeRule.waitForIdle()

        assertNotDisabled("Leaf")
    }

}
