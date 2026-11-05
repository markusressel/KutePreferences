package de.markusressel.kutepreferences.ui.views

import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.preference.bool.KuteMainSwitchPreference
import de.markusressel.kutepreferences.core.preference.category.KuteParent
import de.markusressel.kutepreferences.core.preference.matches
import java.util.IdentityHashMap

/**
 * Knows which items are disabled by a [KuteMainSwitchPreference]. This is based on the tree and not on the current
 * page, because an item needs to be disabled in the search results as well, which do not show its siblings.
 */
internal class MainSwitchConditions {

    private val conditions = IdentityHashMap<KutePreferenceListItem, KuteCondition>()

    /**
     * @return the condition that needs to be met for the [item] to be enabled, or null if it does not depend on any
     */
    operator fun get(item: KutePreferenceListItem): KuteCondition? = conditions[item]

    fun update(items: List<KutePreferenceListItem>) {
        conditions.clear()
        collect(items, inherited = null)
    }

    private fun collect(items: List<KutePreferenceListItem>, inherited: KuteCondition?) {
        val mainSwitches = items.filterIsInstance<KuteMainSwitchPreference>()

        items.forEach { item ->
            val condition = mainSwitches
                .filter { it !== item }
                .map { mainSwitch -> mainSwitch.matches { isOn -> isOn } }
                .plus(listOfNotNull(inherited))
                .reduceOrNull { first, second -> first and second }

            condition?.let { conditions[item] = it }
            if (item is KuteParent) {
                collect(item.children, condition)
            }
        }
    }

}
