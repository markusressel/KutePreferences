package de.markusressel.kutepreferences.ui.views.listitems

import android.content.res.Configuration
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.text.KuteUrlPreference
import de.markusressel.kutepreferences.core.preference.text.UrlPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.TextEditDialog
import de.markusressel.kutepreferences.ui.views.search.dummy

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun UrlPreferencePreview() {
    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    val preferenceItem = KuteUrlPreference(
        key = 0,
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

@Composable
fun UrlPreference(
    behavior: UrlPreferenceBehavior,
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    editDialog: @Composable () -> Unit = {
        TextPreferenceEditDialog(
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
        onClick = {
            dialogState.show()
        }
    )
    editDialog()
}


@Composable
private fun TextPreferenceEditDialog(
    dialogState: MaterialDialogState,
    behavior: UrlPreferenceBehavior,
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
        onInputChanged = { input ->
            value = input
            behavior.onInputChanged(input)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        onCancelClicked = onCancelClicked,
        onDefaultClicked = onDefaultClicked,
        onSaveClicked = onSaveClicked,
        validator = validator
    )
}