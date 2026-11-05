package de.markusressel.kutepreferences.ui.vm

import android.os.Looper
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.action.ActionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.section.KuteSectionBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.search.KutePreferencesScreen
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import java.time.Duration

@RunWith(RobolectricTestRunner::class)
class NavigateToItemTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val navigator = DefaultKuteNavigator()

    private val target = KuteAction(key = "target", title = "Target item")
    private val fillers = List(40) { KuteAction(key = "filler$it", title = "Filler $it") }
    private val innerSection = KuteSection(key = "inner", title = "Inner section", children = fillers + target)
    private val outerSection = KuteSection(key = "outer", title = "Outer section", children = listOf(innerSection))
    private val category = KuteCategory(key = "category", title = "Category", children = listOf(outerSection))

    private val viewModel = KutePreferencesViewModel(navigator).apply {
        initPreferencesTree(listOf(category))
    }

    private fun section(item: KuteSection) = viewModel.registry.behaviorFor(item) as KuteSectionBehavior
    private val targetBehavior get() = viewModel.registry.behaviorFor(target) as ActionPreferenceBehavior

    private fun selectTarget() {
        val result = SearchItemsUseCase.KuteSearchResultItem(key = "0", item = target, searchTerm = "target")
        viewModel.onUiEvent(KuteUiEvent.SearchResultSelected(result))
    }

    private fun showScreen() {
        composeRule.setContent {
            KutePreferencesTheme {
                KutePreferencesScreen(
                    modifier = Modifier.height(400.dp),
                    kuteViewModel = viewModel,
                    contentPadding = PaddingValues(),
                )
            }
        }
    }

    /**
     * The ViewModel waits on the main looper, the UI on the clock of the compose rule.
     */
    private fun selectTargetAndAwaitHighlight() {
        composeRule.mainClock.autoAdvance = false
        composeRule.runOnIdle { selectTarget() }
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(1))
        composeRule.mainClock.advanceTimeBy(1_000)
    }

    @Test
    fun `navigating expands every section on the way and scrolls to the highlighted item`() {
        section(innerSection).collapse()
        section(outerSection).collapse()
        showScreen()

        selectTargetAndAwaitHighlight()

        assertEquals(category.key, navigator.currentCategory.value)
        assertFalse(section(innerSection).uiState.value.isCollapsed)
        assertFalse(section(outerSection).uiState.value.isCollapsed)
        assertTrue(targetBehavior.uiState.value.shimmering)
        composeRule.onNodeWithText("Target item").assertIsDisplayed()
    }

    @Test
    fun `navigating by key opens the category, expands the sections and highlights the item`() {
        section(innerSection).collapse()
        showScreen()

        composeRule.mainClock.autoAdvance = false
        composeRule.runOnIdle { viewModel.navigateTo("target") }
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(1))
        composeRule.mainClock.advanceTimeBy(1_000)

        assertEquals(category.key, navigator.currentCategory.value)
        assertFalse(section(innerSection).uiState.value.isCollapsed)
        assertTrue(targetBehavior.uiState.value.shimmering)
        composeRule.onNodeWithText("Target item").assertIsDisplayed()
    }

    @Test
    fun `navigating by key closes the search even if the category stays open`() {
        val rootItem = KuteAction(key = "root", title = "Root item")
        val newViewModel = KutePreferencesViewModel(DefaultKuteNavigator())
        newViewModel.initPreferencesTree(listOf(rootItem))
        shadowOf(Looper.getMainLooper()).idle()
        newViewModel.onUiEvent(KuteUiEvent.SearchFieldSelected)
        newViewModel.onUiEvent(KuteUiEvent.SearchTermChanged("root"))
        shadowOf(Looper.getMainLooper()).idle()
        assertTrue(newViewModel.preferencesUiState.value.isSearchActive)

        newViewModel.navigateTo("root")
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(1))

        assertFalse(newViewModel.preferencesUiState.value.isSearchActive)
        assertEquals("", newViewModel.preferencesUiState.value.searchTerm)
    }

    @Test
    fun `navigating by key to a category opens it`() {
        showScreen()

        composeRule.runOnIdle { viewModel.navigateTo("category") }
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(1))

        assertEquals(category.key, navigator.currentCategory.value)
    }

    @Test
    fun `navigating to an unknown key does nothing`() {
        showScreen()

        composeRule.runOnIdle { viewModel.navigateTo("unknown") }
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(1))

        assertEquals(null, navigator.currentCategory.value)
        assertFalse(targetBehavior.uiState.value.shimmering)
    }

    @Test
    fun `navigating by key waits until the tree has been loaded`() {
        val loaded = CompletableDeferred<Unit>()
        val slowProvider = object : KutePreferenceDataProvider by DummyDataProvider() {
            override suspend fun load() = loaded.await()
        }
        val item = KuteBooleanPreference(key = "slow", title = "Slow", defaultValue = true, dataProvider = slowProvider)
        val newViewModel = KutePreferencesViewModel(DefaultKuteNavigator())

        newViewModel.initPreferencesTree(
            listOf(KuteCategory(key = "slow_category", title = "Category", children = listOf(item))),
        )
        newViewModel.navigateTo("slow")
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(1))
        assertEquals(null, newViewModel.navigator.currentCategory.value)

        loaded.complete(Unit)
        shadowOf(Looper.getMainLooper()).idleFor(Duration.ofSeconds(1))

        assertEquals("slow_category", newViewModel.navigator.currentCategory.value)
    }

    @Test
    fun `highlight ends after the animation`() {
        showScreen()

        selectTargetAndAwaitHighlight()
        assertTrue(targetBehavior.uiState.value.shimmering)

        composeRule.mainClock.advanceTimeBy(10_000)
        assertFalse(targetBehavior.uiState.value.shimmering)
    }
}
