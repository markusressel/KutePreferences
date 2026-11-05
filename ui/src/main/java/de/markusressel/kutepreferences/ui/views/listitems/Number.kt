package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.NumberPreferenceBehavior
import de.markusressel.kutepreferences.ui.R
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.clickAction
import de.markusressel.kutepreferences.ui.util.dummy
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.TextEditDialog
import de.markusressel.kutepreferences.ui.views.common.rememberCancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.validationErrorText

@Composable
fun NumberPreference(
    behavior: NumberPreferenceBehavior,
    dialogState: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(),
    editDialog: @Composable () -> Unit = { NumberPreferenceEditDialog(behavior, dialogState) }
) {
    DialogPreferenceListItem(behavior, dialogState, editDialog)
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
        visualTransformation = VisualTransformation.None,
        revealable = false,
        errorMessage = when (value.toLongOrNull()) {
            // NOTE: the behavior still has the last number that could be read
            null -> stringResource(R.string.kute_preferences_error_not_a_number)
            else -> behavior.validationErrorText()
        },
        onInputChanged = { input ->
            value = input
            input.toLongOrNull()?.let(behavior::onInputChanged)
        },
        onCancelClicked = onCancelClicked,
        onDefaultClicked = onDefaultClicked,
        onSaveClicked = onSaveClicked,
        validator = { input ->
            input.toLongOrNull()?.let(validator) ?: false
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
                    key = "0",
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
