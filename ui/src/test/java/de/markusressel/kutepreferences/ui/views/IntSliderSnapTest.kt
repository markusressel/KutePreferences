package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.number.slider.IntSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.KuteIntSliderPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.listitems.NumberSliderPreference
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.math.round

@RunWith(RobolectricTestRunner::class)
class IntSliderSnapTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun item(maximum: Long) = KuteIntSliderPreference(
        key = "int",
        title = "Slider",
        minimum = 0,
        maximum = maximum,
        defaultValue = 0,
        dataProvider = DummyDataProvider(),
    )

    private fun show(item: KuteIntSliderPreference) {
        val behavior = IntSliderPreferenceBehavior(item)
        composeRule.setContent { KutePreferencesTheme { NumberSliderPreference(behavior) } }
        composeRule.onNodeWithText("Slider").performClick()
    }

    private fun slider() = composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress))

    /**
     * Taps the slider at the position of [value] of a range from 0 to [maximum], with the thumb in between
     */
    private fun tapAt(value: Float, maximum: Float) {
        slider().performTouchInput {
            val x = THUMB_WIDTH / 2f + (width - THUMB_WIDTH) * value / maximum
            down(Offset(x, centerY))
            up()
        }
    }

    private fun assertOnlyWholeValuesWhileDragging(startValue: Float) {
        slider().performTouchInput { down(center) }
        repeat(DRAG_MOVES) {
            slider().performTouchInput { moveBy(Offset(x = DRAG_MOVE_PX, y = 0f), delayMillis = 100) }
            composeRule.mainClock.advanceTimeBy(100)
            assertEquals(round(shownValue()), shownValue(), 0f)
        }
        assertNotEquals(startValue, shownValue(), 0f)
    }

    private fun shownValue(): Float {
        return slider().fetchSemanticsNode().config[SemanticsProperties.ProgressBarRangeInfo].current
    }

    @Test
    fun `below the middle of two values rounds down`() {
        show(item(maximum = 10))

        tapAt(value = 3.4f, maximum = 10f)

        assertEquals(3f, shownValue(), 0f)
    }

    @Test
    fun `above the middle of two values rounds up`() {
        show(item(maximum = 10))

        tapAt(value = 3.6f, maximum = 10f)

        assertEquals(4f, shownValue(), 0f)
    }

    @Test
    fun `dragging only shows whole values`() {
        show(item(maximum = 10))

        assertOnlyWholeValuesWhileDragging(startValue = 0f)
    }

    @Test
    fun `dragging a slider with two values only shows whole values`() {
        show(item(maximum = 1))

        assertOnlyWholeValuesWhileDragging(startValue = 0f)
    }

    @Test
    fun `releasing the slider saves a whole value`() {
        val item = item(maximum = 10)
        show(item)

        tapAt(value = 3.6f, maximum = 10f)

        composeRule.runOnIdle { assertEquals(4L, item.getCurrentPersistedValue()) }
    }

    private companion object {
        const val THUMB_WIDTH = 4f
        const val DRAG_MOVES = 12
        const val DRAG_MOVE_PX = 7f
    }

}
