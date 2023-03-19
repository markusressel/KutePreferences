package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.preference.category.KuteParent

fun List<KutePreferenceListItem>.filterRecursive(
    searchTerm: String
): List<KutePreferenceListItem> = map {
    when (it) {
        is KuteParent -> {
            it.children.filterRecursive(searchTerm)
        }
        else -> listOf(it)
    }
}.flatten().filter {
    it.search(searchTerm)
}