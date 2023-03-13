package de.markusressel.kutepreferences.core.preference.select

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class MultiSelectionPreferenceBehavior(
    preferenceItem: KuteMultiSelectStringPreference
) : PersistedPreferenceBehavior<KuteMultiSelectStringPreference, Set<String>>(
    preferenceItem
) {

    fun onCancelClicked() = reset()

    fun onDefaultClicked() = restoreDefaultValue()

    fun isValid(): Boolean {
        return true
    }

}