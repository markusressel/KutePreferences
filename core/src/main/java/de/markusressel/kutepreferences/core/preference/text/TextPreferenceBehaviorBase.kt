package de.markusressel.kutepreferences.core.preference.text

import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class TextPreferenceBehaviorBase<T : KutePreferenceItem<String>>(
    preferenceItem: T
) : PersistedPreferenceBehavior<T, String>(
    preferenceItem
) {

    fun onCancelClicked() = reset()

    fun onDefaultClicked() = restoreDefaultValue()

}