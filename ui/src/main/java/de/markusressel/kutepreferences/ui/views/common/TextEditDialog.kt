package de.markusressel.kutepreferences.ui.views.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.vanpra.composematerialdialogs.MaterialDialogState
import de.markusressel.kutepreferences.core.preference.Validator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextEditDialog(
    dialogState: MaterialDialogState,
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
        TextField(
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(),
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
            visualTransformation = visualTransformation,
            isError = isError,
        )
    }
}