package de.markusressel.kutepreferences.ui.views

import android.content.res.Configuration
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.persistence.DummyDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference

val dummy = DummyDataProvider()

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KuteOverview(
    items: List<KutePreferenceListItem>,
    modifier: Modifier = Modifier
) {
    var searching by remember { mutableStateOf(false) }
    var searchTerm by remember { mutableStateOf("") }

    AnimatedContent(
        modifier = modifier,
        targetState = searching,
    ) { isSearching ->
        val searchFocusRequester = remember { FocusRequester() }

        Column {
            if (isSearching) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            searchFocusRequester.freeFocus()
                            searchTerm = ""
                            searching = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    KutePreferenceSearch(
                        modifier.weight(1f),
                        searchTerm = searchTerm,
                        onSearchTermChanged = { searchTerm = it },
                        onSearchClicked = { },
                        onClearSearchTerm = {
                            searchTerm = ""
                        },
                        focusRequester = searchFocusRequester,
                    )
                }
            }

            LaunchedEffect(key1 = isSearching) {
                if (isSearching) {
                    searchFocusRequester.requestFocus()
                }
            }

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                if (isSearching.not()) {
                    KutePreferenceSearch(
                        modifier = Modifier.onFocusChanged {
                            if (it.isFocused || it.isCaptured) {
                                searching = true
                            }
                        },
                        searchTerm = "",
                        onSearchClicked = { searching = true },
                        onSearchTermChanged = { },
                        onClearSearchTerm = { },
                    )
                }

                if (isSearching.not()) {
                    KutePreferenceListContent(
                        modifier = Modifier.fillMaxWidth(),
                        items = items,
                        searchTerm = searchTerm,
                    )
                } else {
                    KutePreferenceSearchingContent(
                        modifier = Modifier.fillMaxWidth(),
                        items = items,
                        searchTerm = searchTerm,
                    )
                }
            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SampleOverviewPreview() {
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
        )
    )
}

@Composable
fun KuteItemList(
    modifier: Modifier = Modifier,
    items: List<KutePreferenceListItem>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items.forEach {
            it.Composable()
        }
    }
}

@Composable
fun KutePreferenceListItem.Composable() {
    val styleManager = remember { KuteStyleManager }
    styleManager.renderComposable(this)
}

