package de.markusressel.kutepreferences.ui.views.search

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.views.KuteStyleManager
import de.markusressel.kutepreferences.ui.vm.SearchItemsUseCase


@Composable
fun KutePreferenceSearchingContent(
    modifier: Modifier = Modifier,
    items: List<SearchItemsUseCase.KuteSearchResultItem>,
    onSearchResultSelected: (SearchItemsUseCase.KuteSearchResultItem) -> Unit,
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


@CombinedPreview
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
            SearchItemsUseCase.KuteSearchResultItem(
                key = "0",
                item = preferenceItem,
                searchTerm = "search term"
            ),
            SearchItemsUseCase.KuteSearchResultItem(
                key = "1",
                item = KuteTextPreference(
                    key = 0,
                    icon = null,
                    title = "Hello World!",
                    defaultValue = "Hey there!",
                    dataProvider = dummy
                ),
                searchTerm = "search term"
            ),
        ),
        onSearchResultSelected = {},
    )
}