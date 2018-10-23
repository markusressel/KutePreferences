package de.markusressel.kutepreferences.preference.number

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialogBase
import de.markusressel.kutepreferences.preference.R

open class KuteSliderPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<Int>,
        private val minimum: Int?,
        private val maximum: Int?) :
        KutePreferenceEditDialogBase<Int>() {
    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__slider__edit_dialog

    var seekBar: SeekBar? = null

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        seekBar = contentView
                .findViewById(R.id.kute_preferences__preference__seekbar)

        userInput = persistedValue

        seekBar?.let {
            if (maximum != null) {
                it.max = maximum
            } else {
                it.max = Int.MAX_VALUE
            }

            it.progress = persistedValue

            it.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    seekBar?.let {
                        val newAsLong = it.progress
                        userInput = newAsLong
                        currentValue = newAsLong
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }
    }

    override fun onCurrentValueChanged(oldValue: Int?, newValue: Int?, byUser: Boolean) {
        if (!byUser) {
            newValue?.let {
                seekBar
                        ?.progress = it
            }
        }
    }

}
