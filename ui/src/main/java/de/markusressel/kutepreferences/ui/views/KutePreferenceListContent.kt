package de.markusressel.kutepreferences.ui.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.filterRecursive


@Composable
fun KutePreferenceListContent(
    modifier: Modifier = Modifier,
    items: List<KutePreferenceListItem>,
    searchTerm: String,
) {
    Column(modifier = modifier) {
        KuteItemList(
            modifier = Modifier.fillMaxWidth(),
            items = items.filterRecursive(searchTerm)
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
    KutePreferenceListContent(
        items = listOf(),
        searchTerm = "hel",
    )
}