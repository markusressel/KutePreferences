package de.markusressel.kutepreferences.core.preference.bool

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class BooleanPreferenceBehavior(
    preferenceItem: KuteBooleanPreference
) : PersistedPreferenceBehavior<KuteBooleanPreference, Boolean>(
    preferenceItem
) {

    override fun onInputChanged(input: Boolean) {
        currentValue.value = input
        persistCurrentValue()
    }

}