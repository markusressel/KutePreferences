package de.markusressel.kutepreferences.core.preference.text

import android.webkit.URLUtil
import org.apache.commons.validator.routines.UrlValidator

open class UrlPreferenceBehavior(
    preferenceItem: KuteUrlPreference
) : TextPreferenceBehaviorBase<KuteUrlPreference>(
    preferenceItem
) {

    override fun isValid(): Boolean {
        val value = currentValue.value
        val urlValidator = UrlValidator(arrayOf("http", "https"))

        return super.isValid() && URLUtil.isValidUrl(value) && urlValidator.isValid(value)
    }
}
