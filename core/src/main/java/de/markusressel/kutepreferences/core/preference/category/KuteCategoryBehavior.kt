package de.markusressel.kutepreferences.core.preference.category

import de.markusressel.kutepreferences.core.KuteNavigator

open class KuteCategoryBehavior(
    val preferenceItem: KuteCategory,
    val navigator: KuteNavigator,
) {
    fun onClick() {
        navigator.enterCategory(preferenceItem.key)
    }
}
