package de.markusressel.kutepreferences.library.preference.select.multi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.children
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.view.edit.KutePreferenceEditDialogBase


open class KuteMultiSelectPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<Set<String>>,
        private val possibleValues: Map<Int, Int>) :
        KutePreferenceEditDialogBase<Set<String>>() {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__multi_select__edit_dialog

    private var checkBoxLayout: ViewGroup? = null

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        checkBoxLayout = contentView
                .findViewById(R.id.kute_preferences__preference__multi_select__contentView) as ViewGroup

        userInput = persistedValue

        // inflate option views
        possibleValues.forEach { key, title ->
            val checkBoxView: CheckBox = layoutInflater.inflate(
                    R.layout.kute_preferences__preference__multi_select__item,
                    checkBoxLayout,
                    false) as CheckBox
            checkBoxView.id = key
            checkBoxView.tag = checkBoxLayout!!.context.getString(key)
            checkBoxView.text = checkBoxLayout!!.context.getString(title)
            checkBoxView.setOnCheckedChangeListener { compoundButton, checked ->
                val keyAsString = compoundButton.tag as String

                currentValue?.let {
                    val newValue = if (checked) {
                        it.plus(keyAsString)
                    } else {
                        it.minus(keyAsString)
                    }

                    userInput = newValue
                    currentValue = newValue
                }
            }

            checkBoxLayout!!.addView(checkBoxView)
        }

        // initial selection
        updateSelection(persistedValue)
    }

    override fun onCurrentValueChanged(oldValue: Set<String>?, newValue: Set<String>?, byUser: Boolean) {
        if (!byUser) {
            // update selected value
            if (newValue != null) {
                updateSelection(newValue)
            }
        }
    }

    private fun updateSelection(values: Set<String>) {
        // initial selection
        checkBoxLayout!!.children.map {
            it as CheckBox
        }.forEach {
            it.isChecked = it.tag in values
        }
    }

}
