package de.markusressel.kutepreferences.ui.views.listitems

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.time.KuteTimePreference
import de.markusressel.kutepreferences.core.preference.time.TimePersistenceModel
import de.markusressel.kutepreferences.core.preference.time.TimePreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialog
import de.markusressel.kutepreferences.ui.views.dummy
import java.time.LocalTime

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
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
fun TimePreference(
    behavior: TimePreferenceBehavior,
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
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

@Composable
private fun TimePreferenceEditDialog(
    behavior: TimePreferenceBehavior,
    state: MaterialDialogState,
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

    LaunchedEffect(key1 = defaultClicked) {
        // workaround for reinitializing the UI with the new "currentValue"
        defaultClicked = false
    }

    CancelDefaultSaveDialog(
        dialogState = state,
        onCancelClicked = onCancelClicked,
        onSaveClicked = onSaveClicked,
        isSavable = isError.not(),
        onDefaultClicked = {
            defaultClicked = true
            onDefaultClicked()
        },
    ) {
        if (defaultClicked.not()) {
            timepicker(
                colors = TimePickerDefaults.colors(
                    activeBackgroundColor = MaterialTheme.colorScheme.primary,
                    inactiveBackgroundColor = MaterialTheme.colorScheme.onBackground.copy(0.3f),
                    activeTextColor = MaterialTheme.colorScheme.onPrimary,
                    inactiveTextColor = MaterialTheme.colorScheme.onBackground,
                    inactivePeriodBackground = Color.Transparent,
                    selectorColor = MaterialTheme.colorScheme.primary,
                    selectorTextColor = MaterialTheme.colorScheme.onPrimary,
                    headerTextColor = MaterialTheme.colorScheme.onBackground,
                    borderColor = MaterialTheme.colorScheme.onBackground,
                ),
                initialTime = LocalTime.of(value.hourOfDay, value.minute),
                is24HourClock = true,
                onTimeChange = { time ->
                    behavior.onInputChanged(
                        TimePersistenceModel(time.hour, time.minute)
                    )
                }
            )
        }
    }
}