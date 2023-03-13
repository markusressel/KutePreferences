package de.markusressel.kutepreferences.core.preference.number

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class NumberPreferenceBehavior(
    preferenceItem: KuteNumberPreference
) : PersistedPreferenceBehavior<KuteNumberPreference, Long>(
    preferenceItem
) {

    fun onDefaultClicked() = restoreDefaultValue()

    fun isValid(): Boolean {
        if (preferenceItem.minimum != null && currentValue.value < preferenceItem.minimum) {
            return false
        }

        if (preferenceItem.maximum != null && currentValue.value > preferenceItem.maximum) {
            return false
        }

        return true
    }
}