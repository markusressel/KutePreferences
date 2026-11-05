package de.markusressel.kutepreferences.core.preference.text

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KuteValidationError

open class TextPreferenceBehaviorBase<T : KutePreferenceItem<String>>(
    preferenceItem: T
) : EditablePreferenceBehavior<T, String>(
    preferenceItem
) {

    override fun checkCurrentValue(): KuteValidationError? {
        val regex = (preferenceItem as? KuteTextPreference)?.regex
        return when {
            regex != null && regex.toRegex().matches(currentValue.value).not() -> KuteValidationError.Invalid
            else -> super.checkCurrentValue()
        }
    }

}
