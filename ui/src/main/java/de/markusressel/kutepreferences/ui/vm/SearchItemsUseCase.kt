package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.filterRecursive

class SearchItemsUseCase {

    operator fun invoke(allItems: List<KutePreferenceListItem>, searchTerm: String): List<KuteSearchResultItem> {
        return when {
            searchTerm.isBlank() -> emptyList()
            else -> allItems.filterRecursive(searchTerm)
        }.mapIndexed { index, item ->
            KuteSearchResultItem(
                key = "${item.key}@$index",
                item = item,
                searchTerm = searchTerm
            )
        }
    }

    data class KuteSearchResultItem(
        val key: String,
        val item: KutePreferenceListItem,
        val searchTerm: String,
    )

}
