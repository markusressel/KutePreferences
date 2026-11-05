package de.markusressel.kutepreferences.ui.views.common

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * A slider with a range indicator and a marker for the current value, which saves the value when it is released
 * and animates to the new position when the value is changed by something else.
 *
 * @param persistedValue the currently saved value
 * @param persistedValues emits the saved value, starting with the current one
 * @param snap converts the raw position of the slider to a value that can be saved
 * @param markerText the text of the marker for a slider position
 * @param onValueChangeFinished called with a new value when the user has released the slider
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ValueSlider(
    persistedValue: Float,
    persistedValues: Flow<Float>,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    snap: (Float) -> Float = { it },
    markerText: (Float) -> String,
    onValueChangeFinished: (Float) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        var touchedByUser by remember { mutableStateOf(false) }
        var isSliderAnimating by remember { mutableStateOf(false) }

        var sliderPosition by remember { mutableFloatStateOf(persistedValue) }

        var animatedSliderPosition by remember { mutableFloatStateOf(sliderPosition) }
        val sliderAnimation by animateFloatAsState(
            targetValue = animatedSliderPosition,
            finishedListener = {
                isSliderAnimating = false
                touchedByUser = false
            },
            label = "Slider Animation",
        )

        LaunchedEffect(persistedValues) {
            persistedValues.drop(1).distinctUntilChanged().collect {
                // animate to new position if change was triggered from external source
                if (touchedByUser.not() || isSliderAnimating) {
                    isSliderAnimating = true
                    sliderPosition = it
                }
                animatedSliderPosition = it
            }
        }

        val sliderValue = when {
            isSliderAnimating -> sliderAnimation
            else -> sliderPosition
        }

        val interactionSource = remember { MutableInteractionSource() }
        val sliderIsDragged by interactionSource.collectIsDraggedAsState()
        val sliderIsPressed by interactionSource.collectIsPressedAsState()

        val markerIsVisible = touchedByUser || isSliderAnimating || sliderIsDragged || sliderIsPressed
        var sliderWidthPx by remember { mutableIntStateOf(0) }

        Box {
            SliderRangeIndicator(
                modifier = Modifier.padding(horizontal = 4.dp),
                valueRange = valueRange,
                text = markerText,
            )

            Slider(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .onSizeChanged { sliderWidthPx = it.width }
                    .padding(horizontal = SliderTrackOverhang),
                value = sliderValue,
                enabled = isSliderAnimating.not(),
                valueRange = valueRange,
                steps = steps,
                onValueChange = {
                    if (isSliderAnimating.not()) {
                        // NOTE: this also keeps a slider without steps in between its values on a valid value
                        sliderPosition = snap(it)

                        if (persistedValue != snap(it)) {
                            touchedByUser = true
                        }
                    }
                },
                onValueChangeFinished = {
                    val snappedPosition = snap(sliderPosition)
                    sliderPosition = snappedPosition

                    if (persistedValue != snappedPosition) {
                        onValueChangeFinished(snappedPosition)
                    }
                },
                interactionSource = interactionSource,
                track = {
                    KuteSliderTrack(
                        activeStart = null,
                        activeEnd = sliderFraction(it.value, it.valueRange),
                        steps = it.steps,
                        enabled = isSliderAnimating.not(),
                    )
                },
            )

            ThumbMarker(
                text = markerText(sliderValue),
                fraction = sliderFraction(sliderValue, valueRange),
                sliderWidthPx = sliderWidthPx,
                isVisible = markerIsVisible,
            )
        }
    }
}

// NOTE: this is the width of the Material 3 slider thumb, which is not exposed
private val SliderThumbWidth = 4.dp
private val SliderThumbGap = 6.dp
private val SliderTrackHeight = 16.dp
private val SliderTrackInnerCornerSize = 2.dp
private val SliderTrackMinPartWidth = 4.dp

/**
 * How far the track extends past the movement of the thumb on each side, so that the dots at the
 * ends of the track are the end stops of the thumb. Sliders are inset by this.
 */
internal val SliderTrackOverhang = 8.dp

/**
 * How far the marker may extend past the slider, which is the padding around it, to not be cut off by the screen.
 */
private val MarkerOverhang = 8.dp

/**
 * @return the position of [value] in [valueRange], between 0 and 1
 */
internal fun sliderFraction(value: Float, valueRange: ClosedFloatingPointRange<Float>): Float {
    val size = valueRange.endInclusive - valueRange.start
    return when {
        size > 0f -> ((value - valueRange.start) / size).coerceIn(0f, 1f)
        else -> 0f
    }
}

/**
 * @return the value of the [stops] that is closest to [value]
 */
internal fun nearestStop(value: Float, stops: List<Float>): Float = stops.minBy { abs(it - value) }

/**
 * Moves one stop towards [target]. A screen reader or keyboard only asks for a small change, which
 * would never get past the [current] value if it was rounded to the nearest stop.
 *
 * @param stops the possible values, in ascending order
 * @return the next value of the [stops] in the direction of [target], or [current] if it did not change
 */
internal fun nextStop(current: Float, target: Float, stops: List<Float>): Float = when {
    target > current -> stops.firstOrNull { it > current } ?: current
    target < current -> stops.lastOrNull { it < current } ?: current
    else -> current
}

/**
 * @return the horizontal center of the thumb of a slider that is inset by [trackOverhang], which moves linearly
 * in between the dots at the ends of the track
 */
internal fun sliderThumbCenter(
    fraction: Float,
    sliderWidth: Float,
    thumbWidth: Float,
    trackOverhang: Float,
): Float {
    val movement = sliderWidth - trackOverhang * 2 - thumbWidth
    return trackOverhang + thumbWidth / 2f + movement * fraction
}

/**
 * A [Marker] above the thumb of a slider, which fades out with a delay after it is hidden.
 */
@Composable
internal fun ThumbMarker(
    text: String,
    fraction: Float,
    sliderWidthPx: Int,
    isVisible: Boolean,
) {
    var markerWidthPx by remember { mutableIntStateOf(0) }
    val alpha by animateFloatAsState(
        targetValue = when {
            isVisible -> 1f
            else -> 0f
        },
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = when {
                isVisible -> 0
                // hide marker after delay
                else -> 2000
            },
            easing = LinearOutSlowInEasing
        ),
        label = "MarkerAlphaAnimation",
    )

    if (alpha > 0f) {
        val overhangPx = with(LocalDensity.current) { MarkerOverhang.roundToPx() }
        val thumbCenterPx = with(LocalDensity.current) {
            sliderThumbCenter(
                fraction = fraction,
                sliderWidth = sliderWidthPx.toFloat(),
                thumbWidth = SliderThumbWidth.toPx(),
                trackOverhang = SliderTrackOverhang.toPx(),
            )
        }
        Marker(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .onGloballyPositioned { coordinates ->
                    markerWidthPx = coordinates.size.width
                }
                .offset {
                    IntOffset(
                        x = (thumbCenterPx - markerWidthPx / 2f).roundToInt()
                            .coerceAtMost(sliderWidthPx - markerWidthPx + overhangPx)
                            .coerceAtLeast(-overhangPx),
                        y = 0,
                    )
                }
                .alpha(alpha),
            text = text,
        )
    }
}

/**
 * The track of a slider, which extends past the thumb by [SliderTrackOverhang] on each side.
 * It shows a dot at every step, including both ends, where the thumb stops.
 *
 * @param activeStart the start of the active part, or `null` if it starts at the beginning of the track
 * @param activeEnd the end of the active part
 * @param stopFractions the positions of the dots between 0 and 1, instead of the evenly spaced [steps]
 */
@Composable
internal fun KuteSliderTrack(
    activeStart: Float?,
    activeEnd: Float,
    steps: Int,
    enabled: Boolean,
    stopFractions: List<Float>? = null,
) {
    val colors = trackColors(enabled)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(SliderTrackHeight)
    ) {
        val direction = when (layoutDirection) {
            LayoutDirection.Rtl -> -1f
            else -> 1f
        }
        scale(scaleX = direction, scaleY = 1f) {
            val overhang = SliderTrackOverhang.toPx()
            val gap = SliderThumbGap.toPx()
            val startThumb = activeStart?.let { size.width * it }
            val endThumb = size.width * activeEnd
            val activeTrack = (startThumb?.let { it + gap } ?: -overhang)..(endThumb - gap)

            startThumb?.let { drawTrackPart(-overhang, it - gap, colors.inactive, outerStart = true) }
            drawTrackPart(activeTrack.start, activeTrack.endInclusive, colors.active, outerStart = startThumb == null)
            drawTrackPart(endThumb + gap, size.width + overhang, colors.inactive, outerEnd = true)

            val fractions = stopFractions ?: (0..steps + 1).map { it.toFloat() / (steps + 1) }
            for (fraction in fractions) {
                val x = size.width * fraction
                val isBelowThumb = abs(x - endThumb) < gap || (startThumb != null && abs(x - startThumb) < gap)
                if (isBelowThumb.not()) {
                    drawCircle(
                        color = when {
                            x in activeTrack -> colors.activeTick
                            else -> colors.inactiveTick
                        },
                        radius = SliderDefaults.TickSize.toPx() / 2f,
                        center = Offset(x, center.y),
                    )
                }
            }
        }
    }
}

private data class SliderTrackColors(
    val active: Color,
    val inactive: Color,
    val activeTick: Color,
    val inactiveTick: Color,
)

@Composable
private fun trackColors(enabled: Boolean): SliderTrackColors {
    val colors = SliderDefaults.colors()
    return when {
        enabled -> SliderTrackColors(
            active = colors.activeTrackColor,
            inactive = colors.inactiveTrackColor,
            activeTick = colors.activeTickColor,
            inactiveTick = colors.inactiveTickColor,
        )

        else -> SliderTrackColors(
            active = colors.disabledActiveTrackColor,
            inactive = colors.disabledInactiveTrackColor,
            activeTick = colors.disabledActiveTickColor,
            inactiveTick = colors.disabledInactiveTickColor,
        )
    }
}

private fun DrawScope.drawTrackPart(
    from: Float,
    to: Float,
    color: Color,
    outerStart: Boolean = false,
    outerEnd: Boolean = false,
) {
    // what is left of the track behind a thumb that is at its end stop isn't worth showing
    if (to - from < SliderTrackMinPartWidth.toPx()) {
        return
    }
    val outerRadius = CornerRadius(size.height / 2f)
    val innerRadius = CornerRadius(SliderTrackInnerCornerSize.toPx())
    val startRadius = when {
        outerStart -> outerRadius
        else -> innerRadius
    }
    val endRadius = when {
        outerEnd -> outerRadius
        else -> innerRadius
    }
    drawPath(
        path = Path().apply {
            addRoundRect(
                RoundRect(
                    left = from,
                    top = 0f,
                    right = to,
                    bottom = size.height,
                    topLeftCornerRadius = startRadius,
                    topRightCornerRadius = endRadius,
                    bottomRightCornerRadius = endRadius,
                    bottomLeftCornerRadius = startRadius,
                )
            )
        },
        color = color,
    )
}

@Composable
internal fun SliderRangeIndicator(
    valueRange: ClosedFloatingPointRange<Float>,
    text: (Float) -> String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(text = text(valueRange.start))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = text(valueRange.endInclusive))
    }
}
