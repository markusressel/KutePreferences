package de.markusressel.kutepreferences.ui.views.listitems

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.bool.BooleanPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.theme.listItemMinHeight
import de.markusressel.kutepreferences.ui.util.highlightingShimmer
import de.markusressel.kutepreferences.ui.util.modifyIf
import de.markusressel.kutepreferences.ui.views.dummy

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun BooleanPreferencePreview() {
    val preferenceItem = KuteBooleanPreference(
        key = 0,
        title = "Boolean Preference",
        descriptionFunction = {
            "The value of this Preference item is: $it"
        },
        dataProvider = dummy,
        defaultValue = true
    )
    KutePreferencesTheme {
        BooleanPreference(
            behavior = BooleanPreferenceBehavior(preferenceItem)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BooleanPreference(
    behavior: BooleanPreferenceBehavior
) {
    val uiState by behavior.uiState.collectAsState()
    val persistedValue by behavior.persistedValue.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = listItemMinHeight)
            .modifyIf(uiState.shimmering) {
                highlightingShimmer()
            }
            .combinedClickable(
                onClick = { behavior.onInputChanged(behavior.persistedValue.value.not()) },
                onLongClick = { }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            DefaultPreferenceListItemCardContent(
                icon = behavior.preferenceItem.icon,
                title = behavior.preferenceItem.title,
                //titleColor = LocalKuteColors.current.defaultItem.titleColor,
                subtitle = behavior.preferenceItem.createDescription(persistedValue),
            )
        }

        Switch(
            modifier = Modifier.padding(horizontal = 12.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                checkedBorderColor = MaterialTheme.colorScheme.primary,
                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            checked = persistedValue,
            onCheckedChange = { behavior.onInputChanged(it) }
        )
    }
}