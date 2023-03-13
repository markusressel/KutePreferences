package de.markusressel.kutepreferences.core.preference.color

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class ColorPreferenceBehavior(
    preferenceItem: KuteColorPreference,
) : PersistedPreferenceBehavior<KuteColorPreference, Int>(
    preferenceItem
) {

    fun onCancelClicked() {
        currentValue.value = preferenceItem.persistedValue.value
    }

    fun onDefaultClicked() = restoreDefaultValue()

    fun isValid(): Boolean {
        // TODO: how to pass in a validation method from the callsite?
        //      should you be able to? or just _have_ to implement a behavior yourself?
        return true
    }

}