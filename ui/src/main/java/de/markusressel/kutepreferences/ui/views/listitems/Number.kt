package de.markusressel.kutepreferences.ui.views.listitems

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.number.NumberPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.common.TextEditDialog
import de.markusressel.kutepreferences.ui.views.dummy

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
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


@Composable
fun NumberPreference(
    behavior: NumberPreferenceBehavior,
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    editDialog: @Composable () -> Unit = { NumberPreferenceEditDialog(behavior, dialogState) }
) {
    val persistedValue by behavior.persistedValue.collectAsState()

    DefaultPreferenceListItem(
        icon = behavior.preferenceItem.icon,
        title = behavior.preferenceItem.title,
        subtitle = behavior.preferenceItem.createDescription(persistedValue),
        onClick = {
            dialogState.show()
        }
    )
    editDialog()
}

@Composable
private fun NumberPreferenceEditDialog(
    behavior: NumberPreferenceBehavior,
    state: MaterialDialogState = rememberMaterialDialogState(),
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