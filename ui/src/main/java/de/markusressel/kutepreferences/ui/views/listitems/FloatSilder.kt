package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.number.slider.FloatSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.KuteFloatSliderPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.InlineExpandablePreferenceView
import de.markusressel.kutepreferences.ui.views.common.Marker
import de.markusressel.kutepreferences.ui.views.search.dummy
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

@Composable
fun DecimalSliderPreference(
    behavior: FloatSliderPreferenceBehavior,
) {
    val uiState by behavior.uiState.collectAsState()
    val persistedValue by behavior.persistedValue.collectAsState()

    var collapsed by remember { mutableStateOf(true) }

    InlineExpandablePreferenceView(
        modifier = Modifier.modifyIf(uiState.shimmering) {
            highlightingShimmer()
        },
        icon = behavior.preferenceItem.icon,
        title = behavior.preferenceItem.title,
        subtitle = behavior.preferenceItem.createDescription(persistedValue),
        content = {
            DecimalSliderEditInputView(
                persistedValue = persistedValue,
                behavior = behavior,
            )
        },
        collapsed = collapsed,
        onToggleCollapsedState = { collapsed = collapsed.not() }
    )
}

@CombinedPreview
@Composable
private fun DecimalSliderPreferencePreview() {
    KutePreferencesTheme {
        DecimalSliderPreference(
            behavior = FloatSliderPreferenceBehavior(
                KuteFloatSliderPreference(
                    key = 0,
                    onClick = {},
                    onLongClick = {},
                    dataProvider = dummy,
                    title = "Decimal Slider Preference",
                    defaultValue = 1.23f,
                    minimum = -10.0f,
                    maximum = 10.0f,
                )
            )
        )
    }
}

@Composable
private fun DecimalSliderEditInputView(
    persistedValue: Float,
    behavior: FloatSliderPreferenceBehavior
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

        LaunchedEffect(key1 = true, block = {
            behavior.persistedValue.drop(1).distinctUntilChanged().collect {
                // animate to new position if change was triggered from external source
                if (touchedByUser.not() || isSliderAnimating) {
                    // Animating because user triggered
                    isSliderAnimating = true
                    sliderPosition = it
                } else {
                    // NOT animating because user triggered
                }
                animatedSliderPosition = it
            }
        })

        val valueRange by remember {
            mutableStateOf((behavior.preferenceItem.minimum..(behavior.preferenceItem.maximum)))
        }

        val sliderValue = when {
            isSliderAnimating -> sliderAnimation
            else -> sliderPosition
        }

        val interactionSource = remember { MutableInteractionSource() }
        val sliderIsDragged by interactionSource.collectIsDraggedAsState()
        val sliderIsPressed by interactionSource.collectIsPressedAsState()

        var markerIsVisible by remember { mutableStateOf(false) }

        // hide marker after delay
        LaunchedEffect(touchedByUser, isSliderAnimating, sliderIsDragged, sliderIsPressed) {
            markerIsVisible = when {
                touchedByUser || isSliderAnimating || sliderIsDragged || sliderIsPressed -> true
                else -> false
            }
        }
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
                onValueChange = {
                    if (isSliderAnimating.not()) {
                        sliderPosition = it

                        val snappedTargetPosition = sliderPosition
                        if (behavior.persistedValue.value != snappedTargetPosition) {
                            touchedByUser = true
                        }
                    }
                },
                onValueChangeFinished = {
                    val snappedTargetPosition = sliderPosition

                    if (sliderPosition != snappedTargetPosition) {
                        sliderPosition = snappedTargetPosition
                    }

                    if (behavior.persistedValue.value != snappedTargetPosition) {
                        behavior.onInputChanged(snappedTargetPosition)
                        behavior.persistCurrentValue()
                    }
                },
                interactionSource = interactionSource
            )

            val markerText = behavior.preferenceItem.createDescription(sliderValue)
            Marker(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .onGloballyPositioned { coordinates ->
                        markerWidthPx = coordinates.size.width
                    }
                    .offset(markerPosition.x.dp, markerPosition.y.dp)
                    .alpha(animatedMarkerAlpha),
                text = markerText,
            )
        }
    }
}

@CombinedPreview
@Composable
private fun DecimalSliderEditInputViewPreview() {
    DecimalSliderEditInputView(
        persistedValue = 2.0f,
        behavior = FloatSliderPreferenceBehavior(
            KuteFloatSliderPreference(
                key = 0,
                onClick = {},
                onLongClick = {},
                dataProvider = dummy,
                title = "Decimal Slider Preference",
                defaultValue = 1.23f,
                minimum = -10.0f,
                maximum = 10.0f,
            )
        )
    )
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
