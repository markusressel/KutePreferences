package de.markusressel.kutepreferences.ui.views

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.text.KuteTextPreference
import de.markusressel.kutepreferences.ui.theme.KutePreferencesTheme
import de.markusressel.kutepreferences.ui.util.CombinedPreview
import de.markusressel.kutepreferences.ui.views.search.Composable
import de.markusressel.kutepreferences.ui.views.search.dummy


@Composable
fun KuteItemList(
    modifier: Modifier = Modifier,
    items: List<KutePreferenceListItem>,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.then(modifier)
    ) {
        items.forEach {
            it.Composable()
        }
    }
}

@CombinedPreview
@Composable
private fun KuteItemListPreview() {
    remember {
        val nav = DefaultKuteNavigator()
        KuteStyleManager.registerDefaultStyles(nav)
        false
    }

    val icon =
        AppCompatResources.getDrawable(LocalContext.current, android.R.drawable.ic_media_next)

    KutePreferencesTheme {
        KuteItemList(
            items = listOf(
                KuteTextPreference(
                    key = 1,
                    icon = icon,
                    title = "Some Text Preference",
                    defaultValue = "Default",
                    dataProvider = dummy
                )
            )
        )
    }
}