package de.markusressel.kutepreferences.library.preference.number

import android.view.View
import android.widget.NumberPicker
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.view.edit.KutePreferenceEditDialogBase

class KuteNumberPreferenceEditDialog(override val preferenceItem: KutePreferenceItem<Long>) :
        KutePreferenceEditDialogBase<Long>() {
    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__number__edit_dialog

    var numberPickerView: NumberPicker? = null

    var userInput: Long = 0

    override fun onContentViewCreated(contentView: View) {
        numberPickerView = contentView
                .findViewById(R.id.kute_preferences__preference__number__picker)

        userInput = persistedValue

        numberPickerView?.let {
            it.maxValue = Int.MAX_VALUE
            it.minValue = 0

            it.value = persistedValue.toInt()

            it.setOnValueChangedListener { numberPicker, oldVal, newValue ->
                val newAsLong = newValue.toLong()
                userInput = newAsLong
                currentValue = newAsLong
            }
        }
    }

    override fun onCurrentValueChanged(oldValue: Long?, newValue: Long?) {
        if (oldValue != newValue && newValue != userInput) {
            newValue?.let {
                numberPickerView
                        ?.value = it.toInt()
            }
        }
    }

}
