package de.markusressel.kutepreferences.ui.views.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogScope
import com.vanpra.composematerialdialogs.MaterialDialogState
import de.markusressel.kutepreferences.ui.theme.LocalKuteColors


@Composable
fun CancelDefaultSaveDialog(
    dialogState: MaterialDialogState,
    onCancelClicked: () -> Unit,
    onDefaultClicked: () -> Unit,
    onSaveClicked: () -> Unit,
    isSavable: Boolean = true,
    content: @Composable MaterialDialogScope.() -> Unit,
) {
    val buttonTextStyle = MaterialTheme.typography.labelMedium.copy(
        color = LocalKuteColors.current.dialog.buttonTextColor
    )

    MaterialDialog(
        backgroundColor = LocalKuteColors.current.dialog.backgroundColor,
        dialogState = dialogState,
        buttons = {
            negativeButton(
                text = stringResource(android.R.string.cancel),
                textStyle = buttonTextStyle,
                onClick = onCancelClicked
            )
            button(
                text = stringResource(id = de.markusressel.kutepreferences.ui.R.string.str_default),
                textStyle = buttonTextStyle,
                onClick = onDefaultClicked
            )
            positiveButton(
                text = stringResource(de.markusressel.kutepreferences.ui.R.string.save),
                textStyle = buttonTextStyle.let {
                    when {
                        isSavable.not() -> it.copy(color = buttonTextStyle.color.copy(alpha = 0.38f))
                        else -> it
                    }
                },
                onClick = {
                    if (isSavable) {
                        onSaveClicked()
                    }
                },
                disableDismiss = isSavable.not()
            )
        },
        onCloseRequest = {
            onCancelClicked()
            it.hide()
        }
    ) {
        content()
    }
}
