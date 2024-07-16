package de.markusressel.kutepreferences.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.views.search.dummy


@Composable
fun KutePreferenceListContent(
    modifier: Modifier = Modifier,
    items: List<KutePreferenceListItem>,
) {
    Column(modifier = modifier) {
        KuteItemList(
            modifier = Modifier.fillMaxWidth(),
            items = items
        )
        Spacer(
            modifier = Modifier
                .defaultMinSize(minHeight = 128.dp)
        )
    }
}

@CombinedPreview
@Composable
private fun KutePreferenceListContentPreview() {
    KutePreferencesTheme {
        remember {
            val nav = DefaultKuteNavigator()
            KuteStyleManager.registerDefaultStyles(nav)
            false
        }

        KutePreferenceListContent(
            items = listOf(
                KuteTextPreference(
                    key = 0,
                    icon = null,
                    title = "Hello World!",
                    defaultValue = "Hey there!",
                    dataProvider = dummy
                )
            ),
        )
    }
}