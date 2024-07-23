package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.NumberPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.TextEditDialog
import de.markusressel.kutepreferences.ui.views.common.rememberCancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.search.dummy

@Composable
fun NumberPreference(
    behavior: NumberPreferenceBehavior,
    dialogState: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(),
    editDialog: @Composable () -> Unit = { NumberPreferenceEditDialog(behavior, dialogState) }
) {
    val uiState by behavior.uiState.collectAsState()
    val persistedValue by behavior.persistedValue.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .modifyIf(uiState.shimmering) {
                highlightingShimmer()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefaultPreferenceListItem(
            icon = behavior.preferenceItem.icon,
            title = behavior.preferenceItem.title,
            subtitle = behavior.preferenceItem.createDescription(persistedValue),
            onClick = { dialogState.show() },
        )
    }

    editDialog()
}


@Composable
private fun NumberPreferenceEditDialog(
    behavior: NumberPreferenceBehavior,
    state: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(),
    label: String = behavior.preferenceItem.title,
    hint: String = "${behavior.preferenceItem.getDefaultValue()}",
    onCancelClicked: () -> Unit = { behavior.reset() },
    onDefaultClicked: () -> Unit = { behavior.onDefaultClicked() },
    onSaveClicked: () -> Unit = { behavior.persistCurrentValue() },
    validator: Validator<Long> = { behavior.isValid() },
) {
    var value by remember {
        mutableStateOf("${behavior.currentValue.value}")
    }

    LaunchedEffect(key1 = null, block = {
        behavior.currentValue.collect {
            value = "$it"
        }
    })

    TextEditDialog(
        dialogState = state,
        label = label,
        hint = hint,
        value = value,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        onInputChanged = { input ->
            value = input
            try {
                behavior.onInputChanged(input.toLong())
            } catch (Exception: Exception) {
                // ignore
            }
        },
        onCancelClicked = onCancelClicked,
        onDefaultClicked = onDefaultClicked,
        onSaveClicked = onSaveClicked,
        validator = {
            validator(it.toLong())
        }
    )
}

@CombinedPreview
@Composable
private fun NumberRangeSliderPreferencePreview() {
    KutePreferencesTheme {
        NumberPreference(
            behavior = NumberPreferenceBehavior(
                KuteNumberPreference(
                    key = 0,
                    onClick = {},
                    onLongClick = {},
                    dataProvider = dummy,
                    title = "Number Slider Preference",
                    defaultValue = 1000,
                    minimum = 0,
                    maximum = 10000,
                )
            )
        )
    }
}
