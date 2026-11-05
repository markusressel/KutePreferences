package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.preference.category.KuteParent

/**
 * @return all items of this tree that match the [searchTerm], parents before their children.
 * Hidden items and their children are left out.
 */
fun List<KutePreferenceListItem>.filterRecursive(
    searchTerm: String
): List<KutePreferenceListItem> = filterRecursive(searchTerm, parentTitles = emptyList())

private fun List<KutePreferenceListItem>.filterRecursive(
    searchTerm: String,
    parentTitles: List<String>,
): List<KutePreferenceListItem> = flatMap { item ->
    val isMatch = item.search(searchTerm) || item.searchContent?.matches(searchTerm, parentTitles) == true
    val matches = listOfNotNull(item.takeIf { isMatch })
    when {
        item.visibleWhen?.isMet == false -> emptyList()
        item is KuteParent -> matches + item.children.filterRecursive(
            searchTerm = searchTerm,
            parentTitles = parentTitles + listOfNotNull(item.searchContent?.title),
        )

        else -> matches
    }
}
