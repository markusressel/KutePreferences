package de.markusressel.kutepreferences.ui.views

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.action.ActionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.KuteUrlPreference
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.UrlPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.search.KutePreferencesScreen
import de.markusressel.kutepreferences.ui.vm.KutePreferencesViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KuteRegistryTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val dataProvider = DummyDataProvider()
    private val registry = KuteRegistry(DefaultKuteNavigator())

    private class CustomAction : KuteAction(key = "custom", title = "Custom action")

    private class UnknownItem : KutePreferenceListItem {
        override val key = "unknown"
        override val onClick: (() -> Unit)? = null
        override val onLongClick: (() -> Unit)? = null
        override fun search(searchTerm: String) = false
    }

    @Test
    fun `behavior is created once per item`() {
        val item = KuteAction(key = "a", title = "A")

        assertSame(registry.behaviorFor(item), registry.behaviorFor(item))
    }

    @Test
    fun `subclass uses its own registration instead of the one of its parent`() {
        val url = KuteUrlPreference(key = "u", title = "U", defaultValue = "", dataProvider = dataProvider)
        val text = KuteTextPreference(key = "t", title = "T", defaultValue = "", dataProvider = dataProvider)

        assertTrue(registry.behaviorFor(url) is UrlPreferenceBehavior)
        assertTrue(registry.behaviorFor(text) is TextPreferenceBehavior)
    }

    @Test
    fun `unregistered subclass falls back to the closest registered parent`() {
        assertTrue(registry.behaviorFor(CustomAction()) is ActionPreferenceBehavior)
    }

    @Test
    fun `unknown type has no behavior and renders a hint`() {
        assertNull(registry.behaviorFor(UnknownItem()))

        composeRule.setContent { registry.Content(UnknownItem()) }

        composeRule.onNodeWithText("Unsupported list item type: UnknownItem").assertIsDisplayed()
    }

    @Test
    fun `later registration replaces the earlier one`() {
        registry.register<KuteAction> { Text("replaced ${it.title}") }

        composeRule.setContent { registry.Content(KuteAction(key = "a", title = "A")) }

        composeRule.onNodeWithText("replaced A").assertIsDisplayed()
    }

    @Test
    fun `registrations and navigation of two screens are independent`() {
        val firstNavigator = DefaultKuteNavigator()
        val secondNavigator = DefaultKuteNavigator()
        val first = KutePreferencesViewModel(firstNavigator).apply {
            registry.register<KuteAction> { Text("first ${it.title}") }
            initPreferencesTree(
                listOf(
                    KuteAction(key = "a", title = "A"),
                    KuteCategory(key = "c", title = "Category"),
                )
            )
        }
        val second = KutePreferencesViewModel(secondNavigator).apply {
            registry.register<KuteAction> { Text("second ${it.title}") }
            initPreferencesTree(
                listOf(
                    KuteAction(key = "a", title = "A"),
                    KuteCategory(key = "c2", title = "Other category"),
                )
            )
        }

        composeRule.setContent {
            KutePreferencesTheme {
                Column {
                    KutePreferencesScreen(kuteViewModel = first, contentPadding = PaddingValues())
                    KutePreferencesScreen(kuteViewModel = second, contentPadding = PaddingValues())
                }
            }
        }

        composeRule.onNodeWithText("first A").assertIsDisplayed()
        composeRule.onNodeWithText("second A").assertIsDisplayed()

        composeRule.onNodeWithText("Category").performClick()
        composeRule.runOnIdle {
            assertEquals("c", firstNavigator.currentCategory.value)
            assertNull(secondNavigator.currentCategory.value)
        }
    }

    @Test
    fun `item without a provided registry fails with a helpful message`() {
        val message = runCatching {
            composeRule.setContent { KuteAction(key = "a", title = "A").Composable() }
        }.exceptionOrNull()?.message

        assertTrue(message.orEmpty().contains("LocalKuteRegistry"))
    }
}
