package de.markusressel.kutepreferences.core.preference.date

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class DatePreferenceBehavior(
    preferenceItem: KuteDatePreference
) : PersistedPreferenceBehavior<KuteDatePreference, Long>(
    preferenceItem
) {

    fun onCancelClicked() {
        currentValue.value = preferenceItem.persistedValue.value
    }

    fun onDefaultClicked() = restoreDefaultValue()

    fun isValid(): Boolean {
        // TODO: implement validation
        return true
    }

}