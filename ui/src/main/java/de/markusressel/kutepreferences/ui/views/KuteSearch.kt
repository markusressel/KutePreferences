package de.markusressel.kutepreferences.ui.views

import android.content.res.Configuration
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme

@Composable
fun KuteSearch(
    modifier: Modifier = Modifier,
    searchTerm: String,
    searchFocusRequester: FocusRequester,
    items: List<KutePreferenceListItem>,
    onCancelSearch: () -> Unit,
    onSearchTermChanged: (String) -> Unit,
    onClearSearchTerm: () -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
//            IconButton(
//                onClick = {
//                    searchFocusRequester.freeFocus()
//                    onCancelSearch()
//                }
//            ) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onBackground,
//                )
//            }

            KutePreferenceSearch(
                modifier = modifier.fillMaxWidth(), //.weight(1f),
                searchTerm = searchTerm,
                onSearchTermChanged = onSearchTermChanged,
                onSearchClicked = { },
                onClearSearchTerm = {
                    onClearSearchTerm()
                    onCancelSearch()
                },
                focusRequester = searchFocusRequester,
            )
        }

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            KutePreferenceSearchingContent(
                modifier = Modifier.fillMaxWidth(),
                items = items,
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun KuteSearchPreview() {
    remember {
        val nav = DefaultKuteNavigator()
        KuteStyleManager.registerDefaultStyles(nav)
        false
    }


    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    KutePreferencesTheme {
        KuteSearch(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            searchTerm = "some search term",
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
            searchFocusRequester = remember { FocusRequester() },
            onCancelSearch = { },
            onSearchTermChanged = { },
            onClearSearchTerm = { },
        )
    }
}