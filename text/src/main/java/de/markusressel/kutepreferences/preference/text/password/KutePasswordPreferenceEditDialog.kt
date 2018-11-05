package de.markusressel.kutepreferences.preference.text.password

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.preference.text.KuteTextPreferenceEditDialog

open class KutePasswordPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<String>,
        regex: String?) :
        KuteTextPreferenceEditDialog(preferenceItem, regex) {

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        super.onContentViewCreated(context, layoutInflater, contentView)

        editTextView?.let {
            it.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            it.setSelection(it.text.length)
        }
    }

}