package de.markusressel.kutepreferences.core.preference.select

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class SingleSelectionPreferenceBehavior(
    preferenceItem: KuteSingleSelectStringPreference
) : PersistedPreferenceBehavior<KuteSingleSelectStringPreference, String>(
    preferenceItem
) {

    fun onCancelClicked() = reset()

    fun onDefaultClicked() = restoreDefaultValue()

    fun isValid(): Boolean {
        return true
    }

}