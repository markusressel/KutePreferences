package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.filterRecursive
import de.markusressel.kutepreferences.core.search.KuteSearchContent

class SearchItemsUseCase {

    operator fun invoke(allItems: List<KutePreferenceListItem>, searchTerm: String): List<KuteSearchResultItem> {
        return when {
            searchTerm.isBlank() -> emptyList()
            // NOTE: the sort is stable, so equally relevant items stay in the order of the tree
            else -> allItems.filterRecursive(searchTerm).sortedByDescending { it.relevance(searchTerm) }
        }.mapIndexed { index, item ->
            KuteSearchResultItem(
                key = "${item.key}@$index",
                item = item,
                searchTerm = searchTerm
            )
        }
    }

    private fun KutePreferenceListItem.relevance(searchTerm: String): Int {
        return searchContent?.relevance(searchTerm) ?: KuteSearchContent.NO_MATCH
    }

    data class KuteSearchResultItem(
        val key: String,
        val item: KutePreferenceListItem,
        val searchTerm: String,
    )

}
