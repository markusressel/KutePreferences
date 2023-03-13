package de.markusressel.kutepreferences.core.preference.time

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class TimePreferenceBehavior(
    preferenceItem: KuteTimePreference
) : PersistedPreferenceBehavior<KuteTimePreference, TimePersistenceModel>(
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