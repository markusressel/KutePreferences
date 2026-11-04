package de.markusressel.kutepreferences.core.preference.text

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

open class TextPreferenceBehaviorBase<T : KutePreferenceItem<String>>(
    preferenceItem: T
) : EditablePreferenceBehavior<T, String>(
    preferenceItem
) {

    override fun isValid(): Boolean {
        val regex = (preferenceItem as? KuteTextPreference)?.regex
        val patternIsMatching = regex?.toRegex()?.matches(currentValue.value) ?: true
        return patternIsMatching && super.isValid()
    }

}
