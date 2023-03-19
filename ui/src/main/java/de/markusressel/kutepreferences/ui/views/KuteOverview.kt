package de.markusressel.kutepreferences.ui.views

import android.content.res.Configuration
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference

@Composable
fun KuteOverview(
    items: List<KutePreferenceListItem>,
    modifier: Modifier = Modifier,
    onSearchStarted: () -> Unit,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .then(modifier)
    ) {
        KutePreferenceSearch(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused || it.isCaptured) {
                    onSearchStarted()
                }
            },
            searchTerm = "",
            onSearchClicked = onSearchStarted,
            onSearchTermChanged = { },
            onClearSearchTerm = { },
        )

        KutePreferenceListContent(
            modifier = Modifier.fillMaxWidth(),
            items = items,
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun KuteOverviewPreview() {
    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    KuteOverview(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        items = listOf(
            KuteSection(
                key = 0,
                title = "Section",
                children = listOf(
                    KuteBooleanPreference(
                        key = 1,
                        icon = icon,
                        title = "A KuteBooleanPreference",
                        defaultValue = true,
                        dataProvider = dummy
                    ),
                    KuteTextPreference(
                        key = 1,
                        icon = icon,
                        title = "A KuteTextPreference",
                        defaultValue = "Default",
                        dataProvider = dummy
                    ),
                    KuteNumberPreference(
                        key = 1,
                        icon = icon,
                        title = "A KuteNumberPreference",
                        defaultValue = 1337,
                        dataProvider = dummy
                    ),
                ),

                ),
            KuteCategory(
                key = 1,
                title = "Category Title",
                description = "This is a description of a category.",
                children = listOf(
                    KuteTextPreference(
                        key = 1,
                        icon = icon,
                        title = "Some Text Preference",
                        defaultValue = "Default",
                        dataProvider = dummy
                    )
                )
            ),
        ),
        onSearchStarted = { },
        scrollState = rememberScrollState(),
    )
}
