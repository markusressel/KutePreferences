package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteParent

class FindItemStackUseCase {

    /**
     * @return the path to the given [item] (ordered from the root to the [item]) in the given tree
     * @throws NoSuchElementException if the [item] is not part of the tree
     */
    operator fun invoke(allItems: List<KutePreferenceListItem>, item: KutePreferenceListItem): List<KutePreferenceListItem> {
        return findPath(allItems, item) ?: throw NoSuchElementException("No element matching ${item.key} found")
    }

    private fun findPath(
        items: List<KutePreferenceListItem>,
        target: KutePreferenceListItem,
    ): List<KutePreferenceListItem>? {
        items.forEach { item ->
            // multiple items can share a key to display the same value, so it does not identify an item
            if (item === target) {
                return listOf(item)
            }
            if (item is KuteParent) {
                findPath(item.children, target)?.let { return listOf(item) + it }
            }
        }
        return null
    }
}
