package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.category.KuteCategoryBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.theme.listItemMinHeight
import de.markusressel.kutepreferences.ui.views.listitems.KuteCategoryView
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CategoryClickTest {

    @get:Rule
    val composeRule = createComposeRule()

    private var clicks = 0
    private var longClicks = 0

    private fun show() {
        val behavior = KuteCategoryBehavior(
            preferenceItem = KuteCategory(
                key = "category",
                title = "Category",
                onClick = { clicks++ },
                onLongClick = { longClicks++ },
            ),
            navigator = DefaultKuteNavigator(),
        )
        composeRule.setContent { KutePreferencesTheme { KuteCategoryView(behavior) } }
    }

    @Test
    fun `a category is a single click target that covers the whole card`() {
        show()

        val targets = composeRule.onAllNodes(hasClickAction(), useUnmergedTree = true)

        targets.assertCountEquals(1)
        targets[0].assertHeightIsAtLeast(listItemMinHeight)
    }

    @Test
    fun `a click calls onClick once`() {
        show()

        composeRule.onNodeWithText("Category").performClick()

        composeRule.runOnIdle {
            assertEquals(1, clicks)
            assertEquals(0, longClicks)
        }
    }

    @Test
    fun `a long click calls onLongClick`() {
        show()

        composeRule.onNodeWithText("Category").performTouchInput { longClick() }

        composeRule.runOnIdle {
            assertEquals(0, clicks)
            assertEquals(1, longClicks)
        }
    }

}
