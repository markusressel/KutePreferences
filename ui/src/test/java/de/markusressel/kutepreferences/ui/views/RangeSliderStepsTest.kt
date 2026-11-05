package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTouchInput
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.KuteIntRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.common.nearestStop
import de.markusressel.kutepreferences.ui.views.common.nextStop
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.math.abs
import kotlin.math.round

@RunWith(RobolectricTestRunner::class)
class RangeSliderStepsTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun intRange(minimum: Long, maximum: Long, default: RangePersistenceModel<Long>) = KuteIntRangePreference(
        key = "int_range",
        title = "Range",
        minimum = minimum,
        maximum = maximum,
        defaultValue = default,
        dataProvider = DummyDataProvider(),
    )

    private fun show(item: KutePreferenceListItem) {
        val registry = KuteRegistry(DefaultKuteNavigator())
        composeRule.setContent { KutePreferencesTheme { registry.Content(item) } }
        composeRule.onNodeWithText("Range").performClick()
    }

    private fun thumbs() = composeRule.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress))

    private fun value(thumb: Int): Float {
        return thumbs()[thumb].fetchSemanticsNode().config[SemanticsProperties.ProgressBarRangeInfo].current
    }

    private fun dragThumb(thumb: Int, onMove: () -> Unit) {
        thumbs()[thumb].performTouchInput { down(center) }
        repeat(DRAG_MOVES) {
            thumbs()[thumb].performTouchInput { moveBy(Offset(x = DRAG_MOVE_PX, y = 0f), delayMillis = 100) }
            composeRule.mainClock.advanceTimeBy(100)
            onMove()
        }
        thumbs()[thumb].performTouchInput { up() }
        composeRule.waitForIdle()
    }

    @Test
    fun `an int range only shows whole values while dragging and saves them`() {
        val item = intRange(minimum = 0, maximum = 10, default = RangePersistenceModel(2, 5))
        show(item)

        dragThumb(thumb = 1) {
            assertEquals(round(value(1)), value(1), 0f)
        }

        assertNotEquals(5f, value(1), 0f)
        composeRule.runOnIdle {
            assertEquals(RangePersistenceModel(2L, value(1).toLong()), item.getCurrentPersistedValue())
        }
    }

    @Test
    fun `an int range with two values only shows whole values while dragging`() {
        show(intRange(minimum = 0, maximum = 1, default = RangePersistenceModel(0, 1)))

        dragThumb(thumb = 0) {
            assertEquals(round(value(0)), value(0), 0f)
        }

        assertEquals(1f, value(0), 0f)
    }

    @Test
    fun `a float range with steps snaps to them`() {
        val item = KuteFloatRangePreference(
            key = "float_range",
            title = "Range",
            minimum = 0f,
            maximum = 1f,
            steps = 3,
            decimalPlaces = 2,
            defaultValue = RangePersistenceModel(0.25f, 0.5f),
            dataProvider = DummyDataProvider(),
        )
        show(item)

        dragThumb(thumb = 1) {
            assertEquals(round(value(1) * 4f) / 4f, value(1), 0.001f)
        }

        assertNotEquals(0.5f, value(1), 0.001f)
    }

    @Test
    fun `a float range without steps has values in between`() {
        show(
            KuteFloatRangePreference(
                key = "float_range",
                title = "Range",
                minimum = 0f,
                maximum = 1f,
                defaultValue = RangePersistenceModel(0.25f, 0.5f),
                dataProvider = DummyDataProvider(),
            )
        )

        var hadValueInBetween = false
        dragThumb(thumb = 1) {
            hadValueInBetween = hadValueInBetween || abs(round(value(1) * 4f) / 4f - value(1)) > 0.001f
        }

        assertEquals(true, hadValueInBetween)
    }

    private fun rangeWithStops(default: RangePersistenceModel<Float> = RangePersistenceModel(0.5f, 1f)) =
        KuteFloatRangePreference(
            key = "float_range",
            title = "Range",
            minimum = 0f,
            maximum = 10f,
            stops = STOPS,
            defaultValue = default,
            dataProvider = DummyDataProvider(),
        )

    @Test
    fun `a float range with stops only has those values while dragging and saves one`() {
        val item = rangeWithStops()
        show(item)

        dragThumb(thumb = 1) {
            assertTrue("${value(1)} is not a stop", value(1) in ALL_STOPS)
        }

        assertNotEquals(1f, value(1), 0f)
        composeRule.runOnIdle {
            assertEquals(RangePersistenceModel(0.5f, value(1)), item.getCurrentPersistedValue())
        }
    }

    @Test
    fun `a tap on the track selects the nearest stop and not the next one`() {
        show(rangeWithStops(RangePersistenceModel(0.5f, 1f)))
        val thumbY = thumbs()[1].fetchSemanticsNode().boundsInRoot.center.y

        // NOTE: the middle of the screen is the value 5, while the next stop of the handle is 2.5
        composeRule.onRoot().performTouchInput { click(Offset(centerX, thumbY)) }
        composeRule.waitForIdle()

        assertEquals(5f, value(1), 0f)
    }

    @Test
    fun `a screen reader moves a handle to the next stop, even with a small change`() {
        val item = rangeWithStops(RangePersistenceModel(0.5f, 1f))
        show(item)

        thumbs()[1].performSemanticsAction(SemanticsActions.SetProgress) { it(1.1f) }
        composeRule.waitForIdle()
        assertEquals(2.5f, value(1), 0f)

        thumbs()[1].performSemanticsAction(SemanticsActions.SetProgress) { it(2.4f) }
        composeRule.waitForIdle()
        assertEquals(1f, value(1), 0f)
    }

    @Test
    fun `a screen reader cannot move the start handle past an end handle that is not on a stop`() {
        val item = rangeWithStops(RangePersistenceModel(1f, 1.5f))
        show(item)

        thumbs()[0].performSemanticsAction(SemanticsActions.SetProgress) { it(1.1f) }
        composeRule.waitForIdle()

        assertEquals(RangePersistenceModel(1.5f, 1.5f), item.getCurrentPersistedValue())
    }

    @Test
    fun `the minimum and maximum are stops without being listed`() {
        show(rangeWithStops(RangePersistenceModel(0.5f, 5f)))

        thumbs()[1].performSemanticsAction(SemanticsActions.SetProgress) { it(5.1f) }
        thumbs()[0].performSemanticsAction(SemanticsActions.SetProgress) { it(0.4f) }
        composeRule.waitForIdle()

        assertEquals(10f, value(1), 0f)
        assertEquals(0f, value(0), 0f)
    }

    @Test
    fun `a saved value that is not a stop stays until it is changed`() {
        show(rangeWithStops(RangePersistenceModel(0.7f, 3f)))

        assertEquals(0.7f, value(0), 0f)
        assertEquals(3f, value(1), 0f)
    }

    @Test
    fun `next stop moves one stop in the direction of the target and stays at the ends`() {
        assertEquals(2.5f, nextStop(current = 1f, target = 1.1f, ALL_STOPS), 0f)
        assertEquals(1f, nextStop(current = 2.5f, target = 2.4f, ALL_STOPS), 0f)
        assertEquals(1f, nextStop(current = 1f, target = 1f, ALL_STOPS), 0f)
        assertEquals(10f, nextStop(current = 10f, target = 11f, ALL_STOPS), 0f)
        assertEquals(0f, nextStop(current = 0f, target = -1f, ALL_STOPS), 0f)
        assertEquals(1f, nextStop(current = 0.7f, target = 0.8f, ALL_STOPS), 0f)
    }

    @Test
    fun `nearest stop rounds to the closest value`() {
        assertEquals(1f, nearestStop(1.7f, ALL_STOPS), 0f)
        assertEquals(2.5f, nearestStop(1.8f, ALL_STOPS), 0f)
    }

    private companion object {
        val STOPS = listOf(0.5f, 1f, 2.5f, 5f)
        val ALL_STOPS = listOf(0f, 0.5f, 1f, 2.5f, 5f, 10f)
        const val DRAG_MOVES = 25
        const val DRAG_MOVE_PX = 7f
    }

}
