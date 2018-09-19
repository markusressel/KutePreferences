package de.markusressel.kutepreferences.library.preference.color

import android.content.Context
import android.support.annotation.ColorInt
import android.view.LayoutInflater
import android.view.View
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.view.edit.KutePreferenceEditDialogBase

open class KuteColorPreferenceEditDialog(
        override val preferenceItem: KuteColorPreference) :
        KutePreferenceEditDialogBase<Int>() {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__color__edit_dialog

    var colorPickerView: View? = null

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        colorPickerView = contentView
                .findViewById(R.id.kute_preferences__preference__color__color_picker)

        userInput = persistedValue
    }

    override fun onCurrentValueChanged(@ColorInt oldValue: Int?, @ColorInt newValue: Int?, byUser: Boolean) {
        if (!byUser) {
            newValue?.let {
                //                colorPickerView
//                        ?.setColor(it)
            }
        }
    }

}
