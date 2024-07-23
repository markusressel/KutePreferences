@file:OptIn(ExperimentalMaterial3Api::class)

package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.delay
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
    val currentValue by behavior.currentValue.collectAsState()

    val isError = remember(currentValue) {
        try {
            validator(currentValue).not()
        } catch (ex: Exception) {
            true
        }
    }

    var defaultClicked by remember { mutableStateOf(false) }
    LaunchedEffect(defaultClicked) {
        // workaround for reinitializing the UI with the new "currentValue"
        delay(300)
        defaultClicked = false
    }

    if (dialogState.isVisible) {
        CancelDefaultSaveDialog(
            dialogState = dialogState,
            onCancelClicked = onCancelClicked,
            onDefaultClicked = {
                defaultClicked = true
                onDefaultClicked()
            },
            onSaveClicked = onSaveClicked,
            isSavable = isError.not(),
        ) {
            AnimatedContent(
                targetState = defaultClicked,
                label = "resetting date picker"
            ) { defaultClicked ->
                if (defaultClicked.not()) {
                    val datePickerState = rememberDatePickerState(
                        initialSelectedDateMillis = LocalDate.ofEpochDay(currentValue).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
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
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 230.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
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