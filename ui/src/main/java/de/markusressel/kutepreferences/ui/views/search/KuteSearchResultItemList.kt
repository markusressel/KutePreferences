package de.markusressel.kutepreferences.ui.views.search

import android.content.res.Configuration
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.views.KuteStyleManager
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun KuteSearchResultItemList(
    modifier: Modifier = Modifier,
    items: List<KutePreferenceListItem>,
    onSearchResultSelected: (KutePreferenceListItem) -> Unit,
) {
    Column(modifier) {
        items.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    it.Composable()
                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(5.dp),
                )

                IconButton(
                    modifier = Modifier.wrapContentWidth(),
                    onClick = { onSearchResultSelected(it) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun KuteSearchResultItemListPreview() {
    remember {
        val nav = DefaultKuteNavigator()
        KuteStyleManager.registerDefaultStyles(nav)
        false
    }

    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    val preferenceItem = KuteTextPreference(
        key = 0,
        icon = null,
        title = "Text Preference",
        dataProvider = dummy,
        defaultValue = "Current Value"
    )

    val preferenceItem2 = KuteDatePreference(
        key = 0,
        icon = null,
        title = "Date Preference",
        dataProvider = dummy,
        defaultValue = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    )

    KutePreferencesTheme {
        KuteSearchResultItemList(
            items = listOf(
//                preferenceItem,
                preferenceItem2
            ),
            onSearchResultSelected = {},
        )
    }
}