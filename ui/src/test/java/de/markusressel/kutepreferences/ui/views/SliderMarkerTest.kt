package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.text.AnnotatedString
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.number.range.FloatRangeSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.common.sliderFraction
import de.markusressel.kutepreferences.ui.views.common.sliderThumbCenter
import de.markusressel.kutepreferences.ui.views.listitems.NumberRangeSliderPreference
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SliderMarkerTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val item = KuteFloatRangePreference(
        key = "range",
        title = "Range",
        minimum = 0f,
        maximum = 10f,
        defaultValue = RangePersistenceModel(min = 2f, max = 5f),
        dataProvider = DummyDataProvider(),
    )

    private fun showRangeSlider() {
        val behavior = FloatRangeSliderPreferenceBehavior(item)
        composeRule.setContent { KutePreferencesTheme { NumberRangeSliderPreference(behavior) } }
        composeRule.onNodeWithText("Range").performClick()
    }

    private fun thumbs() = composeRule.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress))

    private fun marker(text: String) = composeRule.onAllNodes(
        SemanticsMatcher.expectValue(
            SemanticsProperties.Text,
            listOf(AnnotatedString(text)),
        )
    )

    private fun drag(thumb: Int) {
        thumbs()[thumb].performTouchInput {
            down(center)
            // the first move only passes the touch slop
            moveBy(Offset(x = 30f, y = 0f))
            moveBy(Offset(x = 30f, y = 0f))
        }
    }

    private fun assertMarkerShowsValueOf(thumb: Int, startValue: Float) {
        val value = thumbs()[thumb].fetchSemanticsNode().config[SemanticsProperties.ProgressBarRangeInfo].current
        assertNotEquals(startValue, value, DELTA)
        marker(item.formatValue(value)).assertCountEquals(1)
    }

    @Test
    fun `no marker is shown before a handle is touched`() {
        showRangeSlider()

        marker("2").assertCountEquals(0)
        marker("5").assertCountEquals(0)
    }

    @Test
    fun `dragging the start handle shows its value`() {
        showRangeSlider()

        drag(thumb = 0)

        assertMarkerShowsValueOf(thumb = 0, startValue = 2f)
        marker("5").assertCountEquals(0)
    }

    @Test
    fun `dragging the end handle shows its value`() {
        showRangeSlider()

        drag(thumb = 1)

        assertMarkerShowsValueOf(thumb = 1, startValue = 5f)
        marker("2").assertCountEquals(0)
    }

    @Test
    fun `the range labels use the format of the item`() {
        showRangeSlider()

        composeRule.onNodeWithText("0").assertIsDisplayed()
        composeRule.onNodeWithText("10").assertIsDisplayed()
    }

    @Test
    fun `a slider without steps moves its thumb across the whole track`() {
        assertEquals(2f, center(fraction = 0f, hasSteps = false), DELTA)
        assertEquals(50f, center(fraction = 0.5f, hasSteps = false), DELTA)
        assertEquals(98f, center(fraction = 1f, hasSteps = false), DELTA)
    }

    @Test
    fun `a slider with steps insets its inner steps by the track corners`() {
        assertEquals(2f, center(fraction = 0f, hasSteps = true), DELTA)
        assertEquals(30f, center(fraction = 0.25f, hasSteps = true), DELTA)
        assertEquals(98f, center(fraction = 1f, hasSteps = true), DELTA)
    }

    @Test
    fun `the fraction stays in bounds and handles an empty range`() {
        assertEquals(0.25f, sliderFraction(2f, 1f..5f), DELTA)
        assertEquals(1f, sliderFraction(9f, 1f..5f), DELTA)
        assertEquals(0f, sliderFraction(1f, 1f..1f), DELTA)
    }

    private fun center(fraction: Float, hasSteps: Boolean) = sliderThumbCenter(
        fraction = fraction,
        sliderWidth = 100f,
        thumbWidth = 4f,
        trackCornerSize = 8f,
        hasSteps = hasSteps,
    )

    private companion object {
        const val DELTA = 0.01f
    }

}
