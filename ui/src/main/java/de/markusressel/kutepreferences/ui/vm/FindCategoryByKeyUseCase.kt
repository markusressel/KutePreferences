package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory

class FindCategoryByKeyUseCase {

    operator fun invoke(allItems: List<KutePreferenceListItem>, categoryKey: String): KutePreferenceCategory? {
        allItems.forEach { item ->
            if (item is KutePreferenceCategory && item.key == categoryKey) {
                return item
            }
            if (item is KuteParent) {
                invoke(item.children, categoryKey)?.let { return it }
            }
        }
        return null
    }

}
