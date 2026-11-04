package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.markusressel.kutepreferences.core.preference.number.slider.FloatSliderPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.number.slider.KuteFloatSliderPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.clickAction
import de.markusressel.kutepreferences.ui.util.dummy
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.views.common.InlineExpandablePreferenceView
import de.markusressel.kutepreferences.ui.views.common.ValueSlider

@Composable
fun DecimalSliderPreference(
    behavior: FloatSliderPreferenceBehavior,
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
            DecimalSliderEditInputView(
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
private fun DecimalSliderPreferencePreview() {
    KutePreferencesTheme {
        DecimalSliderPreference(
            behavior = FloatSliderPreferenceBehavior(
                KuteFloatSliderPreference(
                    key = "0",
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
    ValueSlider(
        persistedValue = persistedValue,
        persistedValues = behavior.persistedValue,
        valueRange = behavior.preferenceItem.minimum..behavior.preferenceItem.maximum,
        markerText = { behavior.preferenceItem.createDescription(it) },
        onValueChangeFinished = {
            behavior.onInputChanged(it)
            behavior.persistCurrentValue()
        },
    )
}

@CombinedPreview
@Composable
private fun DecimalSliderEditInputViewPreview() {
    DecimalSliderEditInputView(
        persistedValue = 2.0f,
        behavior = FloatSliderPreferenceBehavior(
            KuteFloatSliderPreference(
                key = "0",
                dataProvider = dummy,
                title = "Decimal Slider Preference",
                defaultValue = 1.23f,
                minimum = -10.0f,
                maximum = 10.0f,
            )
        )
    )
}
