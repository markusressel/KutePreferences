package de.markusressel.kutepreferences.ui.views.listitems

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.markusressel.kutepreferences.core.preference.bool.BooleanPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.bool.KuteMainSwitchPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.theme.itemShape
import de.markusressel.kutepreferences.ui.theme.listItemMinHeight
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.util.clickAction
import de.markusressel.kutepreferences.ui.util.dummy
import de.markusressel.kutepreferences.ui.util.highlightingShimmer

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainSwitchPreference(
    behavior: BooleanPreferenceBehavior,
    modifier: Modifier = Modifier,
) {
    val uiState by behavior.uiState.collectAsStateWithLifecycle()
    val persistedValue by behavior.persistedValue.collectAsStateWithLifecycle()

    val containerColor = when {
        persistedValue -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when {
        persistedValue -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = itemShape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = listItemMinHeight)
                .highlightingShimmer(uiState.shimmering, behavior::clearHighlight)
                .combinedClickable(
                    role = Role.Switch,
                    onClick = behavior.preferenceItem.clickAction {
                        behavior.onInputChanged(behavior.persistedValue.value.not())
                    },
                    onLongClick = behavior.preferenceItem.onLongClick,
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = behavior.preferenceItem.title,
                    style = MaterialTheme.typography.titleMedium,
                )

                val description = behavior.preferenceItem.createDescription(persistedValue)
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Switch(
                modifier = Modifier.padding(start = 12.dp),
                checked = persistedValue,
                onCheckedChange = { behavior.onInputChanged(it) },
            )
        }
    }
}

@CombinedPreview
@Composable
private fun MainSwitchPreferencePreview() {
    val preferenceItem = KuteMainSwitchPreference(
        key = "0",
        title = "Use notifications",
        dataProvider = dummy,
        defaultValue = true,
    )
    KutePreferencesTheme {
        MainSwitchPreference(
            behavior = BooleanPreferenceBehavior(preferenceItem),
        )
    }
}
