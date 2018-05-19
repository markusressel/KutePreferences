package de.markusressel.kutepreferences.library.preference.select

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.children
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.view.edit.KutePreferenceEditDialogBase

open class KuteSingleSelectPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<String>,
        private val possibleValues: Map<Int, Int>) :
        KutePreferenceEditDialogBase<String>() {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__single_select__edit_dialog

    private var radioGroupView: RadioGroup? = null

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        radioGroupView = contentView
                .findViewById(R.id.kute_preferences__preference__single_select__radio_group) as RadioGroup

        userInput = persistedValue

        // inflate option views
        possibleValues.forEach { key, title ->
            val radioButtonView: RadioButton = layoutInflater.inflate(
                    R.layout.kute_preferences__preference__single_select__item,
                    radioGroupView,
                    false) as RadioButton
            radioButtonView.id = key
            radioButtonView.tag = radioGroupView!!.context.getString(key)
            radioButtonView.text = radioGroupView!!.context.getString(title)
            radioButtonView.setOnClickListener {
                onRadioButtonClicked(it)
            }

            radioGroupView!!.addView(radioButtonView)
        }

        // initial selection
        radioGroupView?.children?.forEach {
            if (persistedValue == it.tag) {
                radioGroupView?.check(it.id)
            }
        }
    }

    private fun onRadioButtonClicked(view: View) {
        val keyAsString = view.tag as String
        userInput = keyAsString
        currentValue = keyAsString

        radioGroupView?.check(view.id)
    }

    override fun onCurrentValueChanged(oldValue: String?, newValue: String?, byUser: Boolean) {
        if (!byUser) {
            // update selected value
            if (newValue != null) {
                radioGroupView?.children?.forEach {
                    if (newValue == it.tag) {
                        radioGroupView?.check(it.id)
                    }
                }
            }
        }
    }

}
