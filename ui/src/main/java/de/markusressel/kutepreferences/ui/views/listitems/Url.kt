package de.markusressel.kutepreferences.ui.views.listitems

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import de.markusressel.kutepreferences.core.preference.text.KuteUrlPreference
import de.markusressel.kutepreferences.core.preference.text.UrlPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.views.common.CancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.views.common.rememberCancelDefaultSaveDialogState
import de.markusressel.kutepreferences.ui.util.dummy

@Composable
fun UrlPreference(
    behavior: UrlPreferenceBehavior,
    dialogState: CancelDefaultSaveDialogState = rememberCancelDefaultSaveDialogState(),
    editDialog: @Composable () -> Unit = {
        TextPreferenceEditDialog(
            dialogState = dialogState,
            behavior = behavior,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        )
    }
) {
    DialogPreferenceListItem(behavior, dialogState, editDialog)
}

@CombinedPreview
@Composable
private fun UrlPreferencePreview() {
    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    val preferenceItem = KuteUrlPreference(
        key = "0",
        icon = icon,
        title = "Text Preference",
        dataProvider = dummy,
        defaultValue = "Current Value"
    )
    KutePreferencesTheme {
        UrlPreference(
            behavior = UrlPreferenceBehavior(preferenceItem)
        )
    }
}
