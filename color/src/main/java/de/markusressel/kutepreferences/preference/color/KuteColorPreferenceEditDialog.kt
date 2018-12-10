package de.markusressel.kutepreferences.preference.color

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialogBase
import top.defaults.colorpicker.ColorPickerView

open class KuteColorPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<Int>) :
        KutePreferenceEditDialogBase<Int>() {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__color__edit_dialog

    val colorPickerView: ColorPickerView by lazy {
        contentView!!.findViewById<ColorPickerView>(R.id.kute_preferences__preference__color__color_picker)
    }

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        colorPickerView.setInitialColor(persistedValue)

        colorPickerView.subscribe { color, fromUser ->
            if (fromUser) {
                userInput = color
                currentValue = color
            }
        }

        userInput = persistedValue
    }

    override fun onCurrentValueChanged(@ColorInt oldValue: Int?, @ColorInt newValue: Int?, byUser: Boolean) {
        if (!byUser) {
            newValue?.let {
                colorPickerView.setInitialColor(it)
            }
        }
    }

}
