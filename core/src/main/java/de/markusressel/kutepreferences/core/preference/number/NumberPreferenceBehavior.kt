package de.markusressel.kutepreferences.core.preference.number

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior
import de.markusressel.kutepreferences.core.preference.KuteValidationError

open class NumberPreferenceBehavior(
    preferenceItem: KuteNumberPreference
) : EditablePreferenceBehavior<KuteNumberPreference, Long>(
    preferenceItem
) {

    override fun checkCurrentValue(): KuteValidationError? {
        val value = currentValue.value
        val aboveMinimum = preferenceItem.minimum?.let { value >= it } ?: true
        val belowMaximum = preferenceItem.maximum?.let { value <= it } ?: true
        return when {
            aboveMinimum && belowMaximum -> super.checkCurrentValue()
            else -> KuteValidationError.OutOfRange(
                minimum = preferenceItem.minimum?.toLong(),
                maximum = preferenceItem.maximum?.toLong(),
            )
        }
    }

}
