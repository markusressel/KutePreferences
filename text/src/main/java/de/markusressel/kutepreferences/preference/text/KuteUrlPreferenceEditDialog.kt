package de.markusressel.kutepreferences.preference.text

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.webkit.URLUtil
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

open class KuteUrlPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<String>,
        regex: String?) :
        KuteTextPreferenceEditDialog(preferenceItem, regex) {


    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        super.onContentViewCreated(context, layoutInflater, contentView)
        editTextView?.inputType = InputType.TYPE_TEXT_VARIATION_URI
    }

    override fun isValid(value: String?, pattern: Regex?): Boolean {
        val result = super.isValid(value, pattern)
        return result and URLUtil.isValidUrl(value)
    }

}