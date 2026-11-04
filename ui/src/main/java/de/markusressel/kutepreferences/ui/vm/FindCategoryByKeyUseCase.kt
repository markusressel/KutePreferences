package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory

class FindCategoryByKeyUseCase {

    operator fun invoke(allItems: List<KutePreferenceListItem>, categoryKey: String): KutePreferenceCategory? {
        return allItems.firstNotNullOfOrNull { item ->
            when {
                item is KutePreferenceCategory && item.key == categoryKey -> item
                item is KuteParent -> invoke(item.children, categoryKey)
                else -> null
            }
        }
    }

}
