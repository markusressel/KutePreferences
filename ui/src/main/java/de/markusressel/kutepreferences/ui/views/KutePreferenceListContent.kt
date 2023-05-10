package de.markusressel.kutepreferences.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme


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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
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