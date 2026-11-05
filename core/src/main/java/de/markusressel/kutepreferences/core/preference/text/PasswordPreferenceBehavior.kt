package de.markusressel.kutepreferences.core.preference.text

import de.markusressel.kutepreferences.core.preference.KuteValidationError

open class PasswordPreferenceBehavior(
    preferenceItem: KutePasswordPreference
) : TextPreferenceBehaviorBase<KutePasswordPreference>(
    preferenceItem
) {

    override fun checkCurrentValue(): KuteValidationError? {
        val isDefaultRule = preferenceItem.regex == KutePasswordPreference.DEFAULT_REGEX
        return when {
            isDefaultRule && preferenceItem.regex?.toRegex()?.matches(currentValue.value) == false -> {
                KuteValidationError.TooShort(KutePasswordPreference.DEFAULT_MINIMUM_LENGTH)
            }

            else -> super.checkCurrentValue()
        }
    }

}
