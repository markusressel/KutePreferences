package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.preference.category.KuteParent

/**
 * @return all items of this tree that match the [searchTerm], parents before their children.
 * Hidden items and their children are left out.
 */
fun List<KutePreferenceListItem>.filterRecursive(
    searchTerm: String
): List<KutePreferenceListItem> = flatMap { item ->
    val matches = listOf(item).filter { it.search(searchTerm) }
    when {
        item.visibleWhen?.isMet == false -> emptyList()
        item is KuteParent -> matches + item.children.filterRecursive(searchTerm)
        else -> matches
    }
}
