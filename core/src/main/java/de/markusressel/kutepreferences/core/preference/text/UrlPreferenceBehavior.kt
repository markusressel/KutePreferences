package de.markusressel.kutepreferences.core.preference.text

import android.webkit.URLUtil
import de.markusressel.kutepreferences.core.preference.KuteValidationError
import org.apache.commons.validator.routines.UrlValidator

open class UrlPreferenceBehavior(
    preferenceItem: KuteUrlPreference
) : TextPreferenceBehaviorBase<KuteUrlPreference>(
    preferenceItem
) {

    override fun checkCurrentValue(): KuteValidationError? {
        val value = currentValue.value
        val urlValidator = UrlValidator(arrayOf("http", "https"))

        return when {
            URLUtil.isValidUrl(value) && urlValidator.isValid(value) -> super.checkCurrentValue()
            else -> KuteValidationError.InvalidUrl
        }
    }
}
