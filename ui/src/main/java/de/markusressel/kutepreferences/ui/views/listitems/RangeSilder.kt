package de.markusressel.kutepreferences.ui.views.listitems

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.RangeSlider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.number.range.FloatRangeSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.range.KuteFloatRangePreference
import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.InlineExpandablePreferenceView
import de.markusressel.kutepreferences.ui.views.dummy
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun NumberRangeSliderPreferencePreview() {
    KutePreferencesTheme {
        NumberRangeSliderPreference(
            behavior = FloatRangeSliderPreferenceBehavior(
                KuteFloatRangePreference(
                    key = 0,
                    onClick = {},
                    onLongClick = {},
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

@Composable
fun NumberRangeSliderPreference(
    behavior: FloatRangeSliderPreferenceBehavior,
) {
    val uiState by behavior.uiState.collectAsState()
    var collapsed by remember { mutableStateOf(true) }

    val persistedValue by behavior.persistedValue.collectAsState()

    InlineExpandablePreferenceView(
        modifier = Modifier.modifyIf(uiState.shimmering) {
            highlightingShimmer()
        },
        icon = behavior.preferenceItem.icon,
        title = behavior.preferenceItem.title,
        subtitle = behavior.preferenceItem.createDescription(persistedValue),
        content = {
            RangeSliderEditView(behavior)
        },
        collapsed = collapsed,
        onToggleCollapsedState = { collapsed = collapsed.not() }
    )
}

@Composable
fun RangeSliderEditView(
    behavior: FloatRangeSliderPreferenceBehavior,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp)
            .padding(bottom = 8.dp)
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

        RangeSlider(
            value = when {
                animating -> sliderStartAnimation..sliderEndAnimation
                else -> sliderPosition
            },
            enabled = animating.not(),
            valueRange = (behavior.preferenceItem.minimum..(behavior.preferenceItem.maximum)),
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
            }
        )
    }
}
