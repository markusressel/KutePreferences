package de.markusressel.kutepreferences.ui.views.search

import android.content.res.Configuration
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.views.KuteStyleManager


@Composable
fun KutePreferenceSearchingContent(
    modifier: Modifier = Modifier,
    items: List<KutePreferenceListItem>,
    onSearchResultSelected: (KutePreferenceListItem) -> Unit,
) {
    Column(modifier) {
        KuteSearchResultItemList(
            modifier = Modifier.fillMaxWidth(),
            items = items,
            onSearchResultSelected = onSearchResultSelected,
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
private fun KutePreferenceSearchingContentPreview() {
    remember {
        val nav = DefaultKuteNavigator()
        KuteStyleManager.registerDefaultStyles(nav)
        false
    }

    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    val preferenceItem = KuteTextPreference(
        key = 0,
        icon = icon,
        title = "Text Preference",
        dataProvider = dummy,
        defaultValue = "Current Value"
    )

    KutePreferenceSearchingContent(
        items = listOf(
            preferenceItem,
            KuteTextPreference(
                key = 0,
                icon = null,
                title = "Hello World!",
                defaultValue = "Hey there!",
                dataProvider = dummy
            )
        ),
        onSearchResultSelected = {},
    )
}