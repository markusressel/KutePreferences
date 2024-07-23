package de.markusressel.kutepreferences.ui.views

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.bool.KuteBooleanPreference
import de.markusressel.kutepreferences.core.preference.category.KuteCategory
import de.markusressel.kutepreferences.core.preference.number.KuteNumberPreference
import de.markusressel.kutepreferences.core.preference.section.KuteSection
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.views.search.dummy

@Composable
fun KuteOverview(
    items: List<KutePreferenceListItem>,
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .then(modifier)
    ) {
        KutePreferenceListContent(
            modifier = Modifier.fillMaxWidth(),
            items = items,
        )
    }
}


@CombinedPreview
@Composable
private fun KuteOverviewPreview() {
    remember {
        val nav = DefaultKuteNavigator()
        KuteStyleManager.registerDefaultStyles(nav)
        false
    }

    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    KutePreferencesTheme {
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
            scrollState = rememberScrollState(),
        )
    }
}
