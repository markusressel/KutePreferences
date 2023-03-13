package de.markusressel.kutepreferences.core.preference.text

open class PasswordPreferenceBehavior(
    preferenceItem: KutePasswordPreference
) : TextPreferenceBehaviorBase<KutePasswordPreference>(
    preferenceItem
) {
    fun isValid(): Boolean {
        return preferenceItem.regex?.toRegex()?.matches(currentValue.value) ?: true
    }
}