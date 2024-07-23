package de.markusressel.kutepreferences.ui.views.listitems

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.select.KuteSingleSelectStringPreference
import de.markusressel.kutepreferences.core.preference.select.SingleSelectionPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialog
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.rememberCancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.search.dummy

@Composable
fun SingleSelectionPreference(
    behavior: SingleSelectionPreferenceBehavior,
    dialogState: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(),
    editDialog: @Composable () -> Unit = {
        SingleSelectionPreferenceEditDialog(dialogState, behavior)
    }
) {
    val uiState by behavior.uiState.collectAsState()
    val subtitle by behavior.persistedValue.collectAsState()

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
            subtitle = behavior.preferenceItem.createDescription(subtitle),
            onClick = {
                dialogState.show()
            }
        )
    }

    editDialog()
}

@Composable
private fun SingleSelectionPreferenceEditDialog(
    dialogState: CancelDefaultSaveDialogState,
    behavior: SingleSelectionPreferenceBehavior,
    label: String = behavior.preferenceItem.title,
    onCancelClicked: () -> Unit = behavior::onCancelClicked,
    onDefaultClicked: () -> Unit = behavior::onDefaultClicked,
    onSaveClicked: () -> Unit = behavior::persistCurrentValue,
    validator: Validator<String> = { behavior.isValid() },
) {
    var value by remember {
        mutableStateOf(behavior.currentValue.value)
    }

    val isError = remember(value) {
        try {
            validator(value).not()
        } catch (ex: Exception) {
            true
        }
    }

    CancelDefaultSaveDialog(
        dialogState = dialogState,
        onCancelClicked = onCancelClicked,
        onSaveClicked = onSaveClicked,
        isSavable = isError.not(),
        onDefaultClicked = onDefaultClicked,
    ) {

        LaunchedEffect(key1 = null, block = {
            behavior.currentValue.collect {
                value = it
            }
        })

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            text = label
        )

        behavior.preferenceItem.possibleValues.forEach { (key, title) ->
            val stringValue = stringResource(key)
            SelectionOption(
                title = title,
                selected = value == stringValue,
                onClick = {
                    behavior.currentValue.value = stringValue
                }
            )
        }
    }
}

@CombinedPreview
@Composable
private fun SingleSelectionPreferencePreview() {
    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    val context = LocalContext.current

    val preferenceItem = KuteSingleSelectStringPreference(
        context = context,
        key = 0,
        icon = icon,
        title = "Single Selection Preference",
        dataProvider = dummy,
        possibleValues = mapOf(
            0 to 0,
            1 to 1,
            2 to 2,
        ),
        defaultValue = 0,
    )
    KutePreferencesTheme {
        SingleSelectionPreference(
            behavior = SingleSelectionPreferenceBehavior(preferenceItem)
        )
    }
}

@Composable
private fun SelectionOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickable {
            onClick()
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = { onClick() }
        )

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@CombinedPreview
@Composable
private fun SelectionOptionPreview() {
    SelectionOption(
        title = "Option 1",
        selected = true,
        onClick = {},
    )
}
