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
import androidx.compose.material3.Slider
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

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
        var markerWidthPx by remember { mutableIntStateOf(0) }

        var markerPosition by remember { mutableStateOf(Offset.Zero) }
        val animatedMarkerAlpha by animateFloatAsState(
            targetValue = when {
                markerIsVisible -> 1f
                else -> 0f
            },
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = when {
                    markerIsVisible -> 0
                    // hide marker after delay
                    else -> 2000
                },
                easing = LinearOutSlowInEasing
            ),
            label = "MarkerAlphaAnimation",
        )

        Box {
            SliderRangeIndicator(
                modifier = Modifier.padding(horizontal = 4.dp),
                valueRange = valueRange,
            )

            val density = LocalDensity.current

            Slider(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .onGloballyPositioned {
                        val fraction = ((sliderValue - valueRange.start) / (valueRange.endInclusive - valueRange.start))

                        val offsetInDp = with(density) {
                            val sliderThumbDiameterPx = 20.dp.toPx()
                            val sliderWidthMinusPadding = (it.size.width - sliderThumbDiameterPx)
                            (
                                (fraction * sliderWidthMinusPadding)
                                    + ((sliderThumbDiameterPx - markerWidthPx) / 2f)
                                ).toDp()
                        }

                        markerPosition = Offset(x = offsetInDp.value, y = 0f)
                    },
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
                interactionSource = interactionSource
            )

            Marker(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .onGloballyPositioned { coordinates ->
                        markerWidthPx = coordinates.size.width
                    }
                    .offset(markerPosition.x.dp, markerPosition.y.dp)
                    .alpha(animatedMarkerAlpha),
                text = markerText(sliderValue),
            )
        }
    }
}

@Composable
private fun SliderRangeIndicator(
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(text = "${valueRange.start.toInt()}")
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "${valueRange.endInclusive.toInt()}")
    }
}
