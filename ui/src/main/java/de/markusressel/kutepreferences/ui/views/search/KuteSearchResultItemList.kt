package de.markusressel.kutepreferences.ui.views.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.date.KuteDatePreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.views.KuteStyleManager
import de.markusressel.kutepreferences.ui.vm.SearchItemsUseCase
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KuteSearchResultItemList(
    modifier: Modifier = Modifier,
    items: List<SearchItemsUseCase.KuteSearchResultItem>,
    onSearchResultSelected: (SearchItemsUseCase.KuteSearchResultItem) -> Unit,
) {
    LazyColumn(modifier) {
        items(items, key = { it.key }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .animateItemPlacement(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    it.item.Composable()
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

        item {
            Spacer(
                modifier = Modifier
                    .windowInsetsBottomHeight(WindowInsets.systemBars)
                    .defaultMinSize(minHeight = 128.dp)
            )
        }
    }
}

@CombinedPreview
@Composable
private fun KuteSearchResultItemListPreview() {
    remember {
        val nav = DefaultKuteNavigator()
        KuteStyleManager.registerDefaultStyles(nav)
        false
    }

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
                SearchItemsUseCase.KuteSearchResultItem(
                    key = "0",
                    item = preferenceItem2,
                    searchTerm = "search term",
                ),
            ),
            onSearchResultSelected = {},
        )
    }
}