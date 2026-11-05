package de.markusressel.kutepreferences.ui.views.search

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * The size of the panes of a two pane layout, from the start of the layout
 *
 * @param listWidth the width of the pane with the top level
 * @param gap the space between the panes, which is where a hinge is
 */
internal data class TwoPaneLayout(
    val listWidth: Dp,
    val gap: Dp,
)

/**
 * The position of a hinge in the window, in pixels
 */
internal data class WindowHinge(
    val leftPx: Float,
    val rightPx: Float,
) {

    /**
     * @return the position of this hinge measured from the start of a layout, or null if it is outside of that layout
     */
    fun relativeTo(
        layoutLeftPx: Float,
        layoutWidthPx: Float,
        layoutDirection: LayoutDirection,
    ): ClosedFloatingPointRange<Float>? {
        val left = leftPx - layoutLeftPx
        val right = rightPx - layoutLeftPx
        return when {
            left < 0f || right > layoutWidthPx -> null
            layoutDirection == LayoutDirection.Rtl -> (layoutWidthPx - right)..(layoutWidthPx - left)
            else -> left..right
        }
    }

}

/**
 * @param hinge the position of a hinge that divides the layout, measured from its start
 * @return the panes to show, or null if the layout should show a single page. A hinge always divides the panes,
 * as long as both sides have enough space for their content.
 */
internal fun twoPaneLayout(
    width: Dp,
    twoPaneMinWidth: Dp,
    hinge: ClosedRange<Dp>?,
): TwoPaneLayout? {
    val usableHinge = hinge?.takeIf { it.start >= MinPaneWidth && width - it.endInclusive >= MinPaneWidth }
    return when {
        twoPaneMinWidth == Dp.Infinity -> null
        usableHinge != null -> TwoPaneLayout(
            listWidth = usableHinge.start,
            gap = usableHinge.endInclusive - usableHinge.start,
        )

        width >= twoPaneMinWidth -> TwoPaneLayout(listWidth = DefaultListPaneWidth, gap = 0.dp)
        else -> null
    }
}

private val DefaultListPaneWidth = 360.dp
private val MinPaneWidth = 280.dp
