package de.markusressel.kutepreferences.preference.number.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialogBase
import de.markusressel.kutepreferences.preference.number.R
import kotlin.math.roundToInt

open class KuteSliderPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<Int>,
        private val minimum: Int,
        private val maximum: Int) :
        KutePreferenceEditDialogBase<Int>() {
    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__slider__edit_dialog

    var seekBar: RangeSeekBar? = null

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        seekBar = contentView
                .findViewById(R.id.kute_preferences__preference__seekbar)

        userInput = persistedValue

        seekBar?.let {
            it.setIndicatorTextDecimalFormat("0")

            val range: IntRange = minimum..maximum
            it.tickMarkTextArray = range.map {
                it.toFloat()
            }.map {
                "%.0f".format(it)
            }.toTypedArray()

            it.setRange(minimum.toFloat(), maximum.toFloat())
            it.setValue(persistedValue.toFloat())

            it.setOnRangeChangedListener(object : OnRangeChangedListener {
                override fun onRangeChanged(rangeSeekBar: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                    rangeSeekBar?.let {
                        val newAsLong = leftValue.roundToInt()
                        userInput = newAsLong
                        currentValue = newAsLong
                    }
                }

                override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                }

                override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                }
            })
        }
    }

    override fun onCurrentValueChanged(oldValue: Int?, newValue: Int?, byUser: Boolean) {
        if (!byUser) {
            newValue?.let {
                seekBar?.setValue(it.toFloat())
            }
        }
    }

}
