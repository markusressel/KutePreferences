package de.markusressel.kutepreferences.ui.views.listitems

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.text.KutePasswordPreference
import de.markusressel.kutepreferences.core.preference.text.PasswordPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.TextEditDialog
import de.markusressel.kutepreferences.ui.views.common.rememberCancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.search.dummy

@Composable
fun PasswordPreference(
    behavior: PasswordPreferenceBehavior,
    dialogState: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(),
    editDialog: @Composable () -> Unit = {
        PasswordPreferenceEditDialog(
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
private fun PasswordPreferenceEditDialog(
    dialogState: CancelDefaultSaveDialogState,
    behavior: PasswordPreferenceBehavior,
    label: String = behavior.preferenceItem.title,
    hint: String = behavior.preferenceItem.getDefaultValue(),
    onCancelClicked: () -> Unit = behavior::onCancelClicked,
    onDefaultClicked: () -> Unit = behavior::onDefaultClicked,
    onSaveClicked: () -> Unit = behavior::persistCurrentValue,
    validator: Validator<String> = { behavior.isValid() },
) {
    var value by remember {
        mutableStateOf(behavior.currentValue.value)
    }

    LaunchedEffect(key1 = null, block = {
        behavior.currentValue.collect {
            value = it
        }
    })

    TextEditDialog(
        dialogState = dialogState,
        label = label,
        hint = hint,
        value = value,
        visualTransformation = PasswordVisualTransformation(),
        onInputChanged = { input ->
            value = input
            behavior.onInputChanged(input)
        },
        onCancelClicked = onCancelClicked,
        onDefaultClicked = onDefaultClicked,
        onSaveClicked = onSaveClicked,
        validator = validator
    )
}


@CombinedPreview
@Composable
private fun PasswordPreferencePreview() {
    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    val preferenceItem = KutePasswordPreference(
        key = 0,
        icon = icon,
        title = "Password Preference",
        dataProvider = dummy,
        defaultValue = "Current Value"
    )
    KutePreferencesTheme {
        PasswordPreference(
            behavior = PasswordPreferenceBehavior(preferenceItem)
        )
    }
}
