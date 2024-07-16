package de.markusressel.kutepreferences.ui.views.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vanpra.composematerialdialogs.MaterialDialogState
import de.markusressel.kutepreferences.ui.R
import de.markusressel.kutepreferences.ui.theme.LocalKuteColors
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import java.util.*

class CancelDefaultSaveDialogState(
    isVisible: Boolean = true,
) {
    var isVisible by mutableStateOf(isVisible)

    /**
     * Shows the dialog
     */
    fun show() {
        isVisible = true
    }

    /**
     * Clears focus with a given [FocusManager] and then hides the dialog
     *
     * @param focusManager the focus manager of the dialog view
     */
    fun dismiss(focusManager: FocusManager? = null) {
        focusManager?.clearFocus()
        isVisible = false
    }

    companion object {
        /**
         * The default [Saver] implementation for [MaterialDialogState].
         */
        fun Saver(): Saver<CancelDefaultSaveDialogState, *> = Saver(
            save = { state -> state.isVisible },
            restore = ::CancelDefaultSaveDialogState
        )
    }
}

/**
 * Create and [remember] a [CancelDefaultSaveDialogState].
 *
 * @param initialValue the initial showing state of the dialog
 */
@Composable
fun rememberCancelDefaultSaveDialogState(initialValue: Boolean = false): CancelDefaultSaveDialogState {
    return rememberSaveable(saver = CancelDefaultSaveDialogState.Saver()) {
        CancelDefaultSaveDialogState(isVisible = initialValue)
    }
}

@Composable
fun CancelDefaultSaveDialog(
    dialogState: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(false),
    onCancelClicked: () -> Unit,
    onDefaultClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    isSavable: Boolean = true,
    content: @Composable () -> Unit,
) {
    val buttonTextStyle = MaterialTheme.typography.labelMedium.copy(
        color = LocalKuteColors.current.dialog.buttonTextColor
    )

    if (dialogState.isVisible.not()) {
        return
    }

    val focusManager = LocalFocusManager.current

    Dialog(
        onDismissRequest = {
            onCancelClicked()
            dialogState.dismiss(focusManager)
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        DialogContent(
            buttonTextStyle = buttonTextStyle,
            onCancelClicked = {
                onCancelClicked()
                dialogState.dismiss()
            },
            onDefaultClicked = onDefaultClicked,
            onSaveClicked = {
                onSaveClicked()
                dialogState.dismiss()
            },
            isSavable = isSavable,
            content = content
        )
    }
}

@Composable
private fun DialogContent(
    modifier: Modifier = Modifier,
    buttonTextStyle: TextStyle = MaterialTheme.typography.labelMedium.copy(
        color = LocalKuteColors.current.dialog.buttonTextColor
    ),
    onCancelClicked: () -> Unit,
    onDefaultClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    isSavable: Boolean = true,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(),
    ) {
        Column {
            content()

            HorizontalDivider()
            Row(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                DefaultDialogButton(
                    text = stringResource(R.string.str_default),
                    textStyle = buttonTextStyle,
                    onClick = onDefaultClicked
                )

                Spacer(modifier = Modifier.weight(1f))

                NegativeDialogButton(
                    text = stringResource(android.R.string.cancel),
                    textStyle = buttonTextStyle,
                    onClick = onCancelClicked,
                )

                PositiveDialogButton(
                    text = stringResource(R.string.save),
                    textStyle = buttonTextStyle,
                    onClick = onSaveClicked,
                    isSavable = isSavable,
                    positiveButtonEnabled = mapOf("save" to isSavable),
                )
            }
        }
    }
}

@Composable
private fun DefaultDialogButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    onClick: () -> Unit
) {
    val buttonText = text.uppercase(Locale.ROOT)

    TextButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(text = buttonText, style = textStyle)
    }
}

@Composable
private fun PositiveDialogButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    onClick: () -> Unit,
    positiveButtonEnabled: Map<String, Boolean>,
    isSavable: Boolean,
) {
    val buttonText = text.uppercase(Locale.ROOT)
    val buttonEnabled = positiveButtonEnabled.values.all { it }

    TextButton(
        onClick = {
            if (isSavable) {
                onClick()
            }
        },
        modifier = modifier,
        enabled = buttonEnabled,
    ) {
        Text(text = buttonText, style = textStyle.let {
            when {
                isSavable.not() -> it.copy(color = textStyle.color.copy(alpha = 0.38f))
                else -> it
            }
        })
    }
}

@Composable
private fun NegativeDialogButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    onClick: () -> Unit
) {
    val buttonText = text.uppercase(Locale.ROOT)
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = buttonText, style = textStyle)
    }
}

@CombinedPreview
@Composable
private fun DialogContentPreview() {
    DialogContent(
        onCancelClicked = {},
        onDefaultClicked = {},
        onSaveClicked = {},
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Content")
            }
        }
    )
}