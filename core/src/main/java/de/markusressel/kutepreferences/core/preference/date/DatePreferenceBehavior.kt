package de.markusressel.kutepreferences.core.preference.date

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior
import de.markusressel.kutepreferences.core.preference.KuteValidationError

open class DatePreferenceBehavior(
    preferenceItem: KuteDatePreference
) : EditablePreferenceBehavior<KuteDatePreference, Long>(
    preferenceItem
) {

    override fun checkCurrentValue(): KuteValidationError? {
        val value = currentValue.value
        val aboveMinimum = preferenceItem.minimum?.let { value >= it } ?: true
        val belowMaximum = preferenceItem.maximum?.let { value <= it } ?: true
        return when {
            aboveMinimum && belowMaximum -> super.checkCurrentValue()
            else -> KuteValidationError.Invalid
        }
    }

}
