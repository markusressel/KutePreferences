package de.markusressel.kutepreferences.ui.vm

import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

class CheckPreferenceKeysUseCase {

    /**
     * Items of one data provider can share a key to display the same value, but only if they agree on that value.
     * Default values are compared with [Any.equals], so a custom value type needs to implement it.
     *
     * @throws IllegalArgumentException if items with the same key have a different type or default value,
     * which usually is a copy and paste mistake
     */
    operator fun invoke(items: List<KutePreferenceItem<*>>) {
        items.groupBy { it.dataProvider to it.key }.forEach { (_, sharingItems) ->
            val first = sharingItems.first()
            val conflict = sharingItems.firstOrNull { it.getDefaultValue() != first.getDefaultValue() }
            require(conflict == null) {
                "The preference key \"${first.key}\" is used by ${first::class.simpleName} " +
                    "and ${conflict?.let { it::class.simpleName }} with different default values. " +
                    "Items can only share a key if they have the same value type and default value."
            }
        }
    }

}
