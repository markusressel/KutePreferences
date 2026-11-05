package de.markusressel.kutepreferences.ui.views

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.KuteIntRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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

    private companion object {
        const val DRAG_MOVES = 25
        const val DRAG_MOVE_PX = 7f
    }

}
