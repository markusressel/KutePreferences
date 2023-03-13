package de.markusressel.kutepreferences.core.preference.text

open class TextPreferenceBehavior(
    preferenceItem: KuteTextPreference
) : TextPreferenceBehaviorBase<KuteTextPreference>(
    preferenceItem
) {
    fun isValid(): Boolean {
        return preferenceItem.regex?.toRegex()?.matches(currentValue.value) ?: true
    }
}