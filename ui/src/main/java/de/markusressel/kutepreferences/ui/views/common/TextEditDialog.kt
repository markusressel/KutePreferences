package de.markusressel.kutepreferences.ui.views.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import de.markusressel.kutepreferences.ui.R
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview


@Composable
fun TextEditDialog(
    dialogState: CancelDefaultSaveDialogState,
    label: String,
    hint: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onInputChanged: (String) -> Unit,
    value: String,
    onCancelClicked: () -> Unit,
    onDefaultClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    validator: Validator<String>,
) {
    TextEditDialog(
        dialogState = dialogState,
        label = label,
        hint = hint,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        revealable = false,
        errorMessage = null,
        onInputChanged = onInputChanged,
        value = value,
        onCancelClicked = onCancelClicked,
        onDefaultClicked = onDefaultClicked,
        onSaveClicked = onSaveClicked,
        validator = validator,
    )
}

/**
 * @param revealable shows a button in the text field that turns off the [visualTransformation].
 * The value is hidden again every time the dialog is opened.
 * @param errorMessage explains why the value cannot be saved
 */
@Composable
internal fun TextEditDialog(
    dialogState: CancelDefaultSaveDialogState,
    label: String,
    hint: String,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation,
    revealable: Boolean,
    errorMessage: String?,
    onInputChanged: (String) -> Unit,
    value: String,
    onCancelClicked: () -> Unit,
    onDefaultClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    validator: Validator<String>,
) {
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
        errorMessage = errorMessage.takeIf { isError },
        onDefaultClicked = onDefaultClicked,
    ) {
        // NOTE: the content of the dialog only exists while it is open, so this does not outlive it
        var revealed by remember { mutableStateOf(false) }

        TextField(
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(),
            value = value,
            onValueChange = { input ->
                onInputChanged(input)
            },
            label = {
                Text(text = label)
            },
            placeholder = {
                Text(text = hint)
            },
            keyboardOptions = keyboardOptions,
            visualTransformation = when {
                revealed -> VisualTransformation.None
                else -> visualTransformation
            },
            trailingIcon = when {
                revealable -> {
                    { RevealButton(revealed = revealed, onClick = { revealed = revealed.not() }) }
                }

                else -> null
            },
            isError = isError,
        )
    }
}

@Composable
private fun RevealButton(
    revealed: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = when {
                revealed -> Icons.Rounded.VisibilityOff
                else -> Icons.Rounded.Visibility
            },
            contentDescription = stringResource(
                when {
                    revealed -> R.string.kute_preferences_hide_password
                    else -> R.string.kute_preferences_show_password
                }
            ),
        )
    }
}

@CombinedPreview
@Composable
private fun TextEditDialogPreview() {
    KutePreferencesTheme {
        TextEditDialog(
            dialogState = rememberCancelDefaultSaveDialogState(true),
            label = "Label",
            hint = "Hint",
            onInputChanged = {},
            value = "Value",
            onCancelClicked = {},
            onDefaultClicked = {},
            onSaveClicked = {},
            validator = { true }
        )
    }
}