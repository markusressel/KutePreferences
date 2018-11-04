package de.markusressel.kutepreferences.preference.text

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

open class KuteUrlPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<String>,
        regex: String?) :
        KuteTextPreferenceEditDialog(preferenceItem, regex) {


    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        super.onContentViewCreated(context, layoutInflater, contentView)
        editTextView?.inputType = InputType.TYPE_TEXT_VARIATION_URI
    }

}