package de.markusressel.kutepreferences.preference.number

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialogBase
import de.markusressel.kutepreferences.preference.R

open class KuteNumberPreferenceEditDialog(override val preferenceItem: KutePreferenceItem<Long>) :
        KutePreferenceEditDialogBase<Long>() {
    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__number__edit_dialog

    var numberPickerView: NumberPicker? = null

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
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

    override fun onCurrentValueChanged(oldValue: Long?, newValue: Long?, byUser: Boolean) {
        if (!byUser) {
            newValue?.let {
                numberPickerView
                        ?.value = it.toInt()
            }
        }
    }

}
