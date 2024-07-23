@file:OptIn(ExperimentalMaterial3Api::class)

package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.time.KuteTimePreference
import de.markusressel.kutepreferences.core.preference.time.TimePersistenceModel
import de.markusressel.kutepreferences.core.preference.time.TimePreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialog
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.rememberCancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.search.dummy
import java.util.*

@Composable
fun TimePreference(
    behavior: TimePreferenceBehavior,
    dialogState: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(),
    editDialog: @Composable () -> Unit = { TimePreferenceEditDialog(behavior, dialogState) }
) {
    val uiState by behavior.uiState.collectAsState()
    val persistedValue by behavior.persistedValue.collectAsState()

    DefaultPreferenceListItem(
        modifier = Modifier.modifyIf(uiState.shimmering) {
            highlightingShimmer()
        },
        icon = behavior.preferenceItem.icon,
        title = behavior.preferenceItem.title,
        subtitle = behavior.preferenceItem.createDescription(persistedValue),
        onClick = {
            dialogState.show()
        }
    )
    editDialog()
}

@CombinedPreview
@Composable
private fun TimePreferencePreview() {
    KutePreferencesTheme {
        TimePreference(
            behavior = TimePreferenceBehavior(
                preferenceItem = KuteTimePreference(
                    key = 0,
                    title = "Time Preference",
                    defaultValue = TimePersistenceModel(hourOfDay = 13, minute = 37),
                    dataProvider = dummy,
                )
            )
        )
    }
}

@Composable
private fun TimePreferenceEditDialog(
    behavior: TimePreferenceBehavior,
    dialogState: CancelDefaultSaveDialogState,
    label: String = behavior.preferenceItem.title,
    hint: String = "${behavior.preferenceItem.getDefaultValue()}",
    onCancelClicked: () -> Unit = { behavior.reset() },
    onDefaultClicked: () -> Unit = { behavior.onDefaultClicked() },
    onSaveClicked: () -> Unit = { behavior.persistCurrentValue() },
    validator: Validator<TimePersistenceModel> = { behavior.isValid() },
) {
    val value by behavior.currentValue.collectAsState()

    val isError = remember(value) {
        try {
            validator(value).not()
        } catch (ex: Exception) {
            true
        }
    }

    var defaultClicked by remember { mutableStateOf(false) }

    LaunchedEffect(defaultClicked) {
        // workaround for reinitializing the UI with the new "currentValue"
        defaultClicked = false
    }

    val currentValue by behavior.currentValue.collectAsState()

    val currentTime = currentValue.let {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, it.hourOfDay)
            set(Calendar.MINUTE, it.minute)
        }
    }

    CancelDefaultSaveDialog(
        dialogState = dialogState,
        onCancelClicked = onCancelClicked,
        onSaveClicked = onSaveClicked,
        isSavable = isError.not(),
        onDefaultClicked = {
            defaultClicked = true
            onDefaultClicked()
        },
    ) {
        if (defaultClicked.not()) {
            val timePickerState = rememberTimePickerState(
                initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
                initialMinute = currentTime.get(Calendar.MINUTE),
                is24Hour = true,
            )

            LaunchedEffect(timePickerState.minute, timePickerState.hour) {
                behavior.onInputChanged(
                    TimePersistenceModel(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                )
            }

            TimePicker(
                modifier = Modifier.fillMaxWidth(),
                state = timePickerState,
                colors = TimePickerDefaults.colors(),
                layoutType = TimePickerDefaults.layoutType(),
            )
        }
    }
}
