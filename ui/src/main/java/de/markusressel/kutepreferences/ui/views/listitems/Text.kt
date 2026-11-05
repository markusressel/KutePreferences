package de.markusressel.kutepreferences.ui.views.listitems

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehaviorBase
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.dummy
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.TextEditDialog
import de.markusressel.kutepreferences.ui.views.common.rememberCancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.validationErrorText

@Composable
fun TextPreference(
    behavior: TextPreferenceBehavior,
    dialogState: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(),
    editDialog: @Composable () -> Unit = {
        TextPreferenceEditDialog(
            dialogState, behavior
        )
    }
) {
    DialogPreferenceListItem(behavior, dialogState, editDialog)
}

@CombinedPreview
@Composable
private fun TextPreferencePreview() {
    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    val preferenceItem = KuteTextPreference(
        key = "0",
        icon = icon,
        title = "Text Preference",
        dataProvider = dummy,
        defaultValue = "Current Value"
    )
    KutePreferencesTheme {
        TextPreference(
            behavior = TextPreferenceBehavior(preferenceItem)
        )
    }
}

/**
 * The edit dialog of a preference that holds a text
 */
@Composable
internal fun TextPreferenceEditDialog(
    dialogState: CancelDefaultSaveDialogState,
    behavior: TextPreferenceBehaviorBase<*>,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    revealable: Boolean = false,
) {
    var value by remember {
        mutableStateOf(behavior.currentValue.value)
    }

    LaunchedEffect(behavior) {
        behavior.currentValue.collect {
            value = it
        }
    }

    TextEditDialog(
        dialogState = dialogState,
        label = behavior.preferenceItem.title,
        hint = behavior.preferenceItem.getDefaultValue(),
        value = value,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        revealable = revealable,
        errorMessage = behavior.validationErrorText(),
        onInputChanged = { input ->
            value = input
            behavior.onInputChanged(input)
        },
        onCancelClicked = behavior::onCancelClicked,
        onDefaultClicked = behavior::onDefaultClicked,
        onSaveClicked = behavior::persistCurrentValue,
        validator = { behavior.isValid() },
    )
}
