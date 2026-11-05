package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.markusressel.kutepreferences.core.preference.number.range.FloatRangeSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.clickAction
import de.markusressel.kutepreferences.ui.util.dummy
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.views.common.InlineExpandablePreferenceView
import de.markusressel.kutepreferences.ui.views.common.KuteSliderTrack
import de.markusressel.kutepreferences.ui.views.common.SliderTrackOverhang
import de.markusressel.kutepreferences.ui.views.common.ThumbMarker
import de.markusressel.kutepreferences.ui.views.common.sliderFraction
import de.markusressel.kutepreferences.ui.views.common.SliderRangeIndicator
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

@Composable
fun NumberRangeSliderPreference(
    behavior: FloatRangeSliderPreferenceBehavior,
) {
    val uiState by behavior.uiState.collectAsStateWithLifecycle()
    var collapsed by remember { mutableStateOf(true) }

    val persistedValue by behavior.persistedValue.collectAsStateWithLifecycle()

    InlineExpandablePreferenceView(
        modifier = Modifier.highlightingShimmer(uiState.shimmering, behavior::clearHighlight),
        icon = behavior.preferenceItem.icon,
        title = behavior.preferenceItem.title,
        subtitle = behavior.preferenceItem.createDescription(persistedValue),
        content = {
            RangeSliderEditView(behavior)
        },
        collapsed = collapsed,
        onToggleCollapsedState = behavior.preferenceItem.clickAction { collapsed = collapsed.not() },
        onLongClick = behavior.preferenceItem.onLongClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeSliderEditView(
    behavior: FloatRangeSliderPreferenceBehavior,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        var touchedByUser by remember { mutableStateOf(false) }
        var animating by remember { mutableStateOf(false) }

        var sliderPosition: ClosedFloatingPointRange<Float> by remember {
            mutableStateOf(behavior.persistedValue.value.let {
                it.min..it.max
            })
        }

        var animatedSliderPosition by remember {
            mutableStateOf(sliderPosition)
        }

        val visibilityThreshold by remember {
            mutableStateOf((behavior.preferenceItem.maximum - behavior.preferenceItem.minimum) / 1000f)
        }

        val sliderStartAnimation by animateFloatAsState(
            targetValue = animatedSliderPosition.start,
            visibilityThreshold = visibilityThreshold,
            finishedListener = {
                animating = false
                touchedByUser = false
            },
            label = "sliderStartAnimation",
        )

        val sliderEndAnimation by animateFloatAsState(
            targetValue = animatedSliderPosition.endInclusive,
            visibilityThreshold = visibilityThreshold,
            finishedListener = {
                animating = false
                touchedByUser = false
            },
            label = "sliderEndAnimation",
        )

        LaunchedEffect(key1 = true, block = {
            behavior.persistedValue.drop(1).distinctUntilChanged().collect {
                // animate to new position if change was triggered from external source
                if (touchedByUser.not() || animating) {
                    // Animating because user triggered
                    animating = true
                    sliderPosition = it.min..it.max
                } else {
                    // NOT animating because user triggered
                }
                animatedSliderPosition = it.min..it.max
            }
        })

        val valueRange = behavior.preferenceItem.minimum..behavior.preferenceItem.maximum
        val sliderValue = when {
            animating -> sliderStartAnimation..sliderEndAnimation
            else -> sliderPosition
        }

        val startInteractionSource = remember { MutableInteractionSource() }
        val endInteractionSource = remember { MutableInteractionSource() }
        val startIsDragged by startInteractionSource.collectIsDraggedAsState()
        val startIsPressed by startInteractionSource.collectIsPressedAsState()
        val endIsDragged by endInteractionSource.collectIsDraggedAsState()
        val endIsPressed by endInteractionSource.collectIsPressedAsState()

        var sliderWidthPx by remember { mutableIntStateOf(0) }

        SliderRangeIndicator(
            modifier = Modifier.padding(horizontal = 4.dp),
            valueRange = valueRange,
            text = behavior.preferenceItem::formatValue,
        )

        RangeSlider(
            modifier = Modifier
                .padding(top = 14.dp)
                .onSizeChanged { sliderWidthPx = it.width }
                .padding(horizontal = SliderTrackOverhang),
            value = sliderValue,
            enabled = animating.not(),
            valueRange = valueRange,
            onValueChange = {
                if (animating.not()) {
                    sliderPosition = it
                }
            },
            onValueChangeFinished = {
                touchedByUser = true

                behavior.onInputChanged(
                    RangePersistenceModel(
                        min = sliderPosition.start,
                        max = sliderPosition.endInclusive,
                    )
                )
                behavior.persistCurrentValue()
            },
            startInteractionSource = startInteractionSource,
            endInteractionSource = endInteractionSource,
            track = {
                KuteSliderTrack(
                    activeStart = sliderFraction(it.activeRangeStart, it.valueRange),
                    activeEnd = sliderFraction(it.activeRangeEnd, it.valueRange),
                    steps = it.steps,
                    enabled = animating.not(),
                )
            },
        )

        ThumbMarker(
            text = behavior.preferenceItem.formatValue(sliderValue.start),
            fraction = sliderFraction(sliderValue.start, valueRange),
            sliderWidthPx = sliderWidthPx,
            isVisible = animating || startIsDragged || startIsPressed,
        )
        ThumbMarker(
            text = behavior.preferenceItem.formatValue(sliderValue.endInclusive),
            fraction = sliderFraction(sliderValue.endInclusive, valueRange),
            sliderWidthPx = sliderWidthPx,
            isVisible = animating || endIsDragged || endIsPressed,
        )
    }
}

@CombinedPreview
@Composable
private fun NumberRangeSliderPreferencePreview() {
    KutePreferencesTheme {
        NumberRangeSliderPreference(
            behavior = FloatRangeSliderPreferenceBehavior(
                KuteFloatRangePreference(
                    key = "0",
                    dataProvider = dummy,
                    title = "Number Slider Preference",
                    defaultValue = RangePersistenceModel(min = 2f, max = 5f),
                    minimum = 0f,
                    maximum = 10f,
                )
            )
        )
    }
}
