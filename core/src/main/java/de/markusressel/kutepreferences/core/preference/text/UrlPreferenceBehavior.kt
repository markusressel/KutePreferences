package de.markusressel.kutepreferences.core.preference.text

import android.webkit.URLUtil
import org.apache.commons.validator.routines.UrlValidator

open class UrlPreferenceBehavior(
    preferenceItem: KuteUrlPreference
) : TextPreferenceBehaviorBase<KuteUrlPreference>(
    preferenceItem
) {
    fun isValid(): Boolean {
        val value = currentValue.value

        val patternIsMatching = preferenceItem.regex?.toRegex()?.matches(value) ?: true
        val schemes = arrayOf("http", "https")
        val urlValidator = UrlValidator(schemes)

        return patternIsMatching && URLUtil.isValidUrl(value) && urlValidator.isValid(value)
    }
}