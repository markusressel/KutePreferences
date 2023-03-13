package de.markusressel.kutepreferences.core.preference

import de.markusressel.kutepreferences.core.preference.category.KuteParent

fun List<KutePreferenceListItem>.filterRecursive(
    searchTerm: String
): List<KutePreferenceListItem> {
    forEach {
        when (it) {
            is KuteParent -> {
                it.searchTerm = searchTerm.ifBlank { null }
            }
        }
    }
    return filter {
        it.search(searchTerm)
    }
}