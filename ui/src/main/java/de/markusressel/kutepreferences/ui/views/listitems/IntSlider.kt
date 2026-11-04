package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.markusressel.kutepreferences.core.preference.number.slider.IntSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.KuteIntSliderPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.clickAction
import de.markusressel.kutepreferences.ui.util.dummy
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.views.common.InlineExpandablePreferenceView
import de.markusressel.kutepreferences.ui.views.common.ValueSlider
import kotlinx.coroutines.flow.map
import kotlin.math.round

@Composable
fun NumberSliderPreference(
    behavior: IntSliderPreferenceBehavior,
) {
    val uiState by behavior.uiState.collectAsStateWithLifecycle()
    val persistedValue by behavior.persistedValue.collectAsStateWithLifecycle()

    var collapsed by remember { mutableStateOf(true) }

    InlineExpandablePreferenceView(
        modifier = Modifier.highlightingShimmer(uiState.shimmering, behavior::clearHighlight),
        icon = behavior.preferenceItem.icon,
        title = behavior.preferenceItem.title,
        subtitle = behavior.preferenceItem.createDescription(persistedValue),
        content = {
            NumberSliderEditInputView(
                persistedValue = persistedValue,
                behavior = behavior,
            )
        },
        collapsed = collapsed,
        onToggleCollapsedState = behavior.preferenceItem.clickAction { collapsed = collapsed.not() },
        onLongClick = behavior.preferenceItem.onLongClick,
    )
}

@CombinedPreview
@Composable
private fun NumberSliderPreferencePreview() {
    KutePreferencesTheme {
        NumberSliderPreference(
            behavior = IntSliderPreferenceBehavior(
                KuteIntSliderPreference(
                    key = "0",
                    dataProvider = dummy,
                    title = "Number Slider Preference",
                    defaultValue = 5,
                    minimum = -100,
                    maximum = 100,
                )
            )
        )
    }
}

@Composable
private fun NumberSliderEditInputView(
    persistedValue: Long,
    behavior: IntSliderPreferenceBehavior
) {
    val persistedValues = remember(behavior) { behavior.persistedValue.map { it.toFloat() } }

    ValueSlider(
        persistedValue = persistedValue.toFloat(),
        persistedValues = persistedValues,
        valueRange = behavior.preferenceItem.minimum.toFloat()..behavior.preferenceItem.maximum.toFloat(),
        steps = ((behavior.preferenceItem.maximum - behavior.preferenceItem.minimum) - 1).toInt(),
        snap = { round(it) },
        markerText = { "${round(it).toInt()}" },
        onValueChangeFinished = {
            behavior.onInputChanged(it.toLong())
            behavior.persistCurrentValue()
        },
    )
}

@CombinedPreview
@Composable
private fun NumberSliderEditInputViewPreview() {
    NumberSliderEditInputView(
        persistedValue = 2,
        behavior = IntSliderPreferenceBehavior(
            KuteIntSliderPreference(
                key = "0",
                dataProvider = dummy,
                title = "Number Slider Preference",
                defaultValue = 5,
                minimum = -100,
                maximum = 100,
            )
        )
    )
}
