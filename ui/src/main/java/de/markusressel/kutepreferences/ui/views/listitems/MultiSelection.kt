package de.markusressel.kutepreferences.ui.views.listitems

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.select.KuteMultiSelectStringPreference
import de.markusressel.kutepreferences.core.preference.select.MultiSelectionPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialog
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.rememberCancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.search.dummy

@Composable
fun MultiSelectionPreference(
    behavior: MultiSelectionPreferenceBehavior,
    dialogState: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(),
    editDialog: @Composable () -> Unit = {
        MultiSelectionPreferenceEditDialog(
            dialogState, behavior
        )
    }
) {
    val uiState by behavior.uiState.collectAsState()
    val subtitle by behavior.persistedValue.collectAsState()

    DefaultPreferenceListItem(
        modifier = Modifier.modifyIf(uiState.shimmering) {
            highlightingShimmer()
        },
        icon = behavior.preferenceItem.icon,
        title = behavior.preferenceItem.title,
        subtitle = behavior.preferenceItem.createDescription(subtitle),
        onClick = { dialogState.show() },
    )
    editDialog()
}

@Composable
private fun MultiSelectionPreferenceEditDialog(
    dialogState: CancelDefaultSaveDialogState,
    behavior: MultiSelectionPreferenceBehavior,
    onCancelClicked: () -> Unit = behavior::onCancelClicked,
    onDefaultClicked: () -> Unit = behavior::onDefaultClicked,
    onSaveClicked: () -> Unit = behavior::persistCurrentValue,
    validator: Validator<Set<String>> = { behavior.isValid() },
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

        val context = LocalContext.current

        behavior.preferenceItem.possibleValues.forEach { (key, title) ->
            SelectionOption(
                title = remember { context.getString(title) },
                selected = value.contains(context.getString(key)),
                onClick = {
                    val keyString = context.getString(key)
                    if (behavior.currentValue.value.contains(keyString)) {
                        behavior.currentValue.value = behavior.currentValue.value.minus(keyString)
                    } else {
                        behavior.currentValue.value = behavior.currentValue.value.plus(keyString)
                    }
                }
            )
        }
    }
}

@CombinedPreview
@Composable
private fun MultiSelectionPreferencePreview() {
    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    val context = LocalContext.current

    val preferenceItem = KuteMultiSelectStringPreference(
        context = context,
        key = 0,
        icon = icon,
        title = "Multiple Selection Preference",
        dataProvider = dummy,
        possibleValues = mapOf(
            0 to 0,
            1 to 1,
            2 to 2,
        ),
        defaultValue = setOf(0),
    )
    KutePreferencesTheme {
        MultiSelectionPreference(
            behavior = MultiSelectionPreferenceBehavior(preferenceItem)
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
        Checkbox(
            checked = selected,
            onCheckedChange = { onClick() }
        )

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
