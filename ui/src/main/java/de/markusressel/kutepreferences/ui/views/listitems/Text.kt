package de.markusressel.kutepreferences.ui.views.listitems

import android.content.res.Configuration
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import de.markusressel.kutepreferences.core.preference.Validator
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.core.preference.text.TextPreferenceBehavior
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.common.TextEditDialog
import de.markusressel.kutepreferences.ui.views.search.dummy

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TextPreferencePreview() {
    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    val preferenceItem = KuteTextPreference(
        key = 0,
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

@Composable
fun TextPreference(
    behavior: TextPreferenceBehavior,
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
    behavior: TextPreferenceBehavior,
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
        onCancelClicked = onCancelClicked,
        onDefaultClicked = onDefaultClicked,
        onSaveClicked = onSaveClicked,
        validator = validator
    )
}