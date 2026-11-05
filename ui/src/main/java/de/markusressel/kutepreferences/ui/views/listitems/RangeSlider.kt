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
import de.markusressel.kutepreferences.core.preference.number.range.IntRangeSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.KuteIntRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.clickAction
import de.markusressel.kutepreferences.ui.util.dummy
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.views.common.InlineExpandablePreferenceView
import de.markusressel.kutepreferences.ui.views.common.KuteSliderTrack
import de.markusressel.kutepreferences.ui.views.common.SliderRangeIndicator
import de.markusressel.kutepreferences.ui.views.common.SliderTrackOverhang
import de.markusressel.kutepreferences.ui.views.common.ThumbMarker
import de.markusressel.kutepreferences.ui.views.common.sliderFraction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlin.math.round

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

@Composable
fun RangeSliderEditView(
    behavior: FloatRangeSliderPreferenceBehavior,
) {
    val persistedValues = remember(behavior) { behavior.persistedValue.map { it.min..it.max } }

    RangeSliderContent(
        persistedValues = persistedValues,
        persistedValue = remember(behavior) { behavior.persistedValue.value.let { it.min..it.max } },
        valueRange = behavior.preferenceItem.minimum..behavior.preferenceItem.maximum,
        steps = behavior.preferenceItem.steps,
        snap = { it },
        text = behavior.preferenceItem::formatValue,
        onValueChangeFinished = {
            behavior.onInputChanged(RangePersistenceModel(min = it.start, max = it.endInclusive))
            behavior.persistCurrentValue()
        },
    )
}

@Composable
fun IntRangeSliderPreference(
    behavior: IntRangeSliderPreferenceBehavior,
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
            IntRangeSliderEditView(behavior)
        },
        collapsed = collapsed,
        onToggleCollapsedState = behavior.preferenceItem.clickAction { collapsed = collapsed.not() },
        onLongClick = behavior.preferenceItem.onLongClick,
    )
}

@Composable
private fun IntRangeSliderEditView(
    behavior: IntRangeSliderPreferenceBehavior,
) {
    val persistedValues = remember(behavior) {
        behavior.persistedValue.map { it.min.toFloat()..it.max.toFloat() }
    }
    val minimum = behavior.preferenceItem.minimum
    val maximum = behavior.preferenceItem.maximum

    RangeSliderContent(
        persistedValues = persistedValues,
        persistedValue = remember(behavior) {
            behavior.persistedValue.value.let { it.min.toFloat()..it.max.toFloat() }
        },
        valueRange = minimum.toFloat()..maximum.toFloat(),
        steps = (maximum - minimum - 1).toInt().coerceAtLeast(0),
        snap = { round(it) },
        text = { "${round(it).toLong()}" },
        onValueChangeFinished = {
            behavior.onInputChanged(RangePersistenceModel(min = it.start.toLong(), max = it.endInclusive.toLong()))
            behavior.persistCurrentValue()
        },
    )
}

/**
 * @param persistedValues emits the saved value, starting with the current one
 * @param persistedValue the currently saved value
 * @param snap converts the raw position of a handle to a value that can be saved
 * @param text the text for a value
 * @param onValueChangeFinished called with the new value when the user has released a handle
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RangeSliderContent(
    persistedValues: Flow<ClosedFloatingPointRange<Float>>,
    persistedValue: ClosedFloatingPointRange<Float>,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    snap: (Float) -> Float,
    text: (Float) -> String,
    onValueChangeFinished: (ClosedFloatingPointRange<Float>) -> Unit,
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
            mutableStateOf(persistedValue)
        }

        var animatedSliderPosition by remember {
            mutableStateOf(sliderPosition)
        }

        val visibilityThreshold by remember {
            mutableStateOf((valueRange.endInclusive - valueRange.start) / 1000f)
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
            persistedValues.drop(1).distinctUntilChanged().collect {
                // animate to new position if change was triggered from external source
                if (touchedByUser.not() || animating) {
                    // Animating because user triggered
                    animating = true
                    sliderPosition = it
                } else {
                    // NOT animating because user triggered
                }
                animatedSliderPosition = it
            }
        })

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
            text = text,
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
                    sliderPosition = snap(it.start)..snap(it.endInclusive)
                }
            },
            onValueChangeFinished = {
                touchedByUser = true

                onValueChangeFinished(sliderPosition)
            },
            startInteractionSource = startInteractionSource,
            endInteractionSource = endInteractionSource,
            steps = steps,
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
            text = text(sliderValue.start),
            fraction = sliderFraction(sliderValue.start, valueRange),
            sliderWidthPx = sliderWidthPx,
            isVisible = animating || startIsDragged || startIsPressed,
        )
        ThumbMarker(
            text = text(sliderValue.endInclusive),
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

@CombinedPreview
@Composable
private fun IntRangeSliderPreferencePreview() {
    KutePreferencesTheme {
        IntRangeSliderPreference(
            behavior = IntRangeSliderPreferenceBehavior(
                KuteIntRangePreference(
                    key = "0",
                    dataProvider = dummy,
                    title = "Int Range Slider Preference",
                    defaultValue = RangePersistenceModel(min = 2, max = 5),
                    minimum = 0,
                    maximum = 10,
                )
            )
        )
    }
}
