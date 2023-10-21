package de.markusressel.kutepreferences.ui.views.listitems

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.date.DatePreferenceBehavior
import de.markusressel.kutepreferences.core.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialog
import de.markusressel.kutepreferences.ui.views.search.dummy
import java.time.LocalDate

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun DatePreferencePreview() {
    KutePreferencesTheme {
        DatePreference(
            behavior = DatePreferenceBehavior(
                preferenceItem = KuteDatePreference(
                    key = 0,
                    title = "Date Preference",
                    defaultValue = 0L,
                    dataProvider = dummy,
                )
            )
        )
    }
}

@Composable
fun DatePreference(
    behavior: DatePreferenceBehavior,
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    editDialog: @Composable () -> Unit = { DatePreferenceEditDialog(behavior, dialogState) }
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
        onClick = { dialogState.show() },
    )
    editDialog()
}

@Composable
private fun DatePreferenceEditDialog(
    behavior: DatePreferenceBehavior,
    state: MaterialDialogState,
    label: String = behavior.preferenceItem.title,
    hint: String = "${behavior.preferenceItem.getDefaultValue()}",
    onCancelClicked: () -> Unit = { behavior.reset() },
    onDefaultClicked: () -> Unit = { behavior.onDefaultClicked() },
    onSaveClicked: () -> Unit = { behavior.persistCurrentValue() },
    validator: Validator<Long> = { behavior.isValid() },
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
            datepicker(
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = MaterialTheme.colorScheme.primary,
                    headerTextColor = MaterialTheme.colorScheme.onPrimary,
                    calendarHeaderTextColor = MaterialTheme.colorScheme.onBackground,
                    dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                    dateInactiveBackgroundColor = MaterialTheme.colorScheme.background,
                    dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                    dateInactiveTextColor = MaterialTheme.colorScheme.onBackground,
                ),
                initialDate = LocalDate.ofEpochDay(value),
                allowedDateValidator = {
                    validator(it.toEpochDay())
                },
                onDateChange = { date ->
                    behavior.onInputChanged(date.toEpochDay())
                }
            )
        }
    }
}