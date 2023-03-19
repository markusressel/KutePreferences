package de.markusressel.kutepreferences.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem


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