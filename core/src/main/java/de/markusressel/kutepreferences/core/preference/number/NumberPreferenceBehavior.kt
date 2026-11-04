package de.markusressel.kutepreferences.core.preference.number

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior

open class NumberPreferenceBehavior(
    preferenceItem: KuteNumberPreference
) : EditablePreferenceBehavior<KuteNumberPreference, Long>(
    preferenceItem
) {

    override fun isValid(): Boolean {
        val value = currentValue.value
        val aboveMinimum = preferenceItem.minimum?.let { value >= it } ?: true
        val belowMaximum = preferenceItem.maximum?.let { value <= it } ?: true
        return aboveMinimum && belowMaximum && super.isValid()
    }

}
