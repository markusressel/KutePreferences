package de.markusressel.kutepreferences.ui.views.common

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
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
                    .onSizeChanged { sliderWidthPx = it.width },
                value = sliderValue,
                enabled = isSliderAnimating.not(),
                valueRange = valueRange,
                steps = steps,
                onValueChange = {
                    if (isSliderAnimating.not()) {
                        sliderPosition = it

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
                track = { KuteSliderTrack(it) },
            )

            ThumbMarker(
                text = markerText(sliderValue),
                thumbCenterPx = with(LocalDensity.current) {
                    sliderThumbCenter(
                        fraction = sliderFraction(sliderValue, valueRange),
                        sliderWidth = sliderWidthPx.toFloat(),
                        thumbWidth = SliderThumbWidth.toPx(),
                        trackCornerSize = SliderTrackCornerSize.toPx(),
                        hasSteps = steps > 0,
                    )
                },
                sliderWidthPx = sliderWidthPx,
                isVisible = markerIsVisible,
            )
        }
    }
}

// NOTE: these are the sizes of the Material 3 slider, which does not expose them
private val SliderThumbWidth = 4.dp
private val SliderTrackCornerSize = 8.dp

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
 * @return the horizontal center of the thumb of a Material 3 slider.
 * It moves across the whole track, except that a slider with steps has its inner steps inset by the track corners.
 */
internal fun sliderThumbCenter(
    fraction: Float,
    sliderWidth: Float,
    thumbWidth: Float,
    trackCornerSize: Float,
    hasSteps: Boolean,
): Float {
    val trackWidth = sliderWidth - thumbWidth
    val offset = when {
        hasSteps && fraction > 0f && fraction < 1f -> (trackWidth - trackCornerSize * 2) * fraction + trackCornerSize
        else -> trackWidth * fraction
    }
    return thumbWidth / 2f + offset
}

/**
 * A [Marker] above the thumb of a slider, which fades out with a delay after it is hidden.
 */
@Composable
internal fun ThumbMarker(
    text: String,
    thumbCenterPx: Float,
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
 * A [ThumbMarker] above one of the thumbs of a range slider, which has no steps
 */
@Composable
internal fun RangeThumbMarker(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    text: (Float) -> String,
    sliderWidthPx: Int,
    isVisible: Boolean,
) {
    ThumbMarker(
        text = text(value),
        thumbCenterPx = with(LocalDensity.current) {
            sliderThumbCenter(
                fraction = sliderFraction(value, valueRange),
                sliderWidth = sliderWidthPx.toFloat(),
                thumbWidth = SliderThumbWidth.toPx(),
                trackCornerSize = SliderTrackCornerSize.toPx(),
                hasSteps = false,
            )
        },
        sliderWidthPx = sliderWidthPx,
        isVisible = isVisible,
    )
}

/**
 * Removes the indicators at both ends of the track, because the thumb moves past them.
 * The ends are shown by the track itself.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun KuteSliderTrack(state: SliderState) {
    SliderDefaults.Track(
        sliderState = state,
        drawStopIndicator = null,
        drawTick = { offset, color -> drawInnerTick(offset, color) },
    )
}

/**
 * See [KuteSliderTrack]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun KuteSliderTrack(state: RangeSliderState) {
    SliderDefaults.Track(
        rangeSliderState = state,
        drawStopIndicator = null,
        drawTick = { offset, color -> drawInnerTick(offset, color) },
    )
}

private fun DrawScope.drawInnerTick(offset: Offset, color: Color) {
    val trackCornerSize = size.height / 2f
    val isAtEnd = abs(offset.x - trackCornerSize) < 1f || abs(offset.x - (size.width - trackCornerSize)) < 1f
    if (isAtEnd.not()) {
        with(SliderDefaults) { drawStopIndicator(offset = offset, size = TickSize, color = color) }
    }
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
