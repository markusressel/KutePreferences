package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory

class FindCategoryByKeyUseCase {

    operator fun invoke(allItems: List<KutePreferenceListItem>, categoryKey: Int): KutePreferenceCategory? {
        return allItems.findRecursive { it.key == categoryKey } as KutePreferenceCategory?
    }


    private fun List<KutePreferenceListItem>.findRecursive(
        predicate: (KutePreferenceListItem) -> Boolean
    ): KutePreferenceListItem? {
        val topLevelResult = firstOrNull(predicate)
        if (topLevelResult != null) {
            return topLevelResult
        }

        forEach {
            val itemResult = it.findRecursive(predicate)
            if (itemResult != null) {
                return itemResult
            }
        }

        return null
    }

    private fun KutePreferenceListItem.findRecursive(
        predicate: (KutePreferenceListItem) -> Boolean
    ): KutePreferenceListItem? {
        return when (this) {
            predicate -> this
            is KuteParent -> children.findRecursive(predicate)
            else -> null
        }
    }


}