package de.markusressel.kutepreferences.library.preference.text

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.view.edit.KutePreferenceEditDialogBase

class KuteTextPreferenceEditDialog(override val preferenceItem: KutePreferenceItem<String>) :
        KutePreferenceEditDialogBase<String>() {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__edit_dialog

    var editTextView: EditText? = null

    override fun onContentViewCreated(contentView: View) {
        editTextView = contentView
                .findViewById(R.id.kute_preferences__preference__text__edit_name)
        editTextView
                ?.setText(currentValue)

        editTextView
                ?.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(text: Editable?) {
                        text
                                ?.let {
                                    currentValue = it
                                            .toString()
                                }
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }
                })
    }

    override fun onCurrentValueChanged(oldValue: String?, newValue: String?) {
        if (oldValue != newValue) {
            editTextView
                    ?.setText(newValue)
        }
    }

}