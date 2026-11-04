package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.preference.category.KuteParent

/**
 * @return all items of this tree that match the [searchTerm], parents before their children
 */
fun List<KutePreferenceListItem>.filterRecursive(
    searchTerm: String
): List<KutePreferenceListItem> = flatMap { item ->
    val matches = listOf(item).filter { it.search(searchTerm) }
    when (item) {
        is KuteParent -> matches + item.children.filterRecursive(searchTerm)
        else -> matches
    }
}
