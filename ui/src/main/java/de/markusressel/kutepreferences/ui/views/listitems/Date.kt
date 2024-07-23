@file:OptIn(ExperimentalMaterial3Api::class)

package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.date.DatePreferenceBehavior
import de.markusressel.kutepreferences.core.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialog
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.rememberCancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.search.dummy
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@Composable
fun DatePreference(
    behavior: DatePreferenceBehavior,
    dialogState: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(),
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
    dialogState: CancelDefaultSaveDialogState,
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

    if (dialogState.isVisible) {
        CancelDefaultSaveDialog(
            dialogState = dialogState,
            onCancelClicked = onCancelClicked,
            onDefaultClicked = onDefaultClicked,
            onSaveClicked = onSaveClicked,
            isSavable = isError.not(),
        ) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = LocalDate.ofEpochDay(value).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            )

            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let {
                    val instant = Instant.ofEpochMilli(it)
                    behavior.onInputChanged(LocalDate.ofInstant(instant, ZoneId.systemDefault()).toEpochDay())
                }
            }

            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(top = 16.dp),
                        text = label
                    )
                },
            )
        }
    }
}

@CombinedPreview
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
@CombinedPreview
private fun CancelDefaultSaveDialog() {
    KutePreferencesTheme {
        DatePreferenceEditDialog(
            behavior = DatePreferenceBehavior(
                preferenceItem = KuteDatePreference(
                    key = 0,
                    title = "Date Preference",
                    defaultValue = 0L,
                    dataProvider = dummy,
                )
            ),
            dialogState = rememberCancelDefaultSaveDialogState(initialIsVisibile = true)
        )
    }
}