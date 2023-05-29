package de.markusressel.kutepreferences.demo.domain

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class CustomBooleanPreferenceBehavior(
    preferenceItem: CustomBooleanPreference
) : PersistedPreferenceBehavior<CustomBooleanPreference, Boolean>(
    preferenceItem
) {

    override fun onInputChanged(input: Boolean) {
        currentValue.value = input
        persistCurrentValue()
    }

}