package de.markusressel.kutepreferences.ui.views.listitems

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.markusressel.kutepreferences.core.preference.action.ActionPreferenceBehavior
import de.markusressel.kutepreferences.core.preference.action.KuteAction
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.theme.listItemMinHeight

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ActionPreferencePreview() {
    val preferenceItem = KuteAction(
        key = 0,
        title = "Action Preference",
        description = "This is an action Preference.",
        onClick = {}
    )
    KutePreferencesTheme {
        ActionPreference(
            behavior = ActionPreferenceBehavior(preferenceItem)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActionPreference(
    behavior: ActionPreferenceBehavior
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = listItemMinHeight)
            .combinedClickable(
                onClick = { behavior.onClick() },
                onLongClick = { behavior.onLongClick() },
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefaultPreferenceListItemCardContent(
            icon = behavior.preferenceItem.icon,
            title = behavior.preferenceItem.title,
            subtitle = behavior.preferenceItem.description,
        )
    }
}