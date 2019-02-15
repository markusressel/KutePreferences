package de.markusressel.kutepreferences.preference.number.range

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import de.markusressel.kutepreferences.preference.number.R
import kotlin.math.roundToInt

open class KuteIntRangePreferenceEditDialog(
        preferenceItem: KuteRangePreference<Int>,
        minimum: Int,
        maximum: Int) :
        KuteRangePreferenceEditDialog<Int>(
                preferenceItem,
                minimum,
                maximum) {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__int_range__edit_dialog

    var rangeSeekBar: RangeSeekBar? = null

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        rangeSeekBar = contentView
                .findViewById(R.id.kute_preferences__preference__range_seekbar)

        userInput = persistedValue

        rangeSeekBar?.let {
            it.setIndicatorTextDecimalFormat("0")

            val stepSize = (maximum - minimum) / 10
            it.tickMarkTextArray = (0..10).mapIndexed { index, item ->
                minimum + stepSize * index
            }.map {
                it.toFloat()
            }.map {
                "%.0f".format(it)
            }.toTypedArray()

            it.setRange(minimum.toFloat(), maximum.toFloat(), 1.toFloat())
            it.setValue(persistedValue.min.toFloat(), persistedValue.max.toFloat())

            it.setOnRangeChangedListener(object : OnRangeChangedListener {
                override fun onRangeChanged(rangeSeekBar1: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                    rangeSeekBar1?.let {
                        val newValue = RangePersistenceModel(leftValue.roundToInt(), rightValue.roundToInt())

                        if (isFromUser) {
                            it.setValue(newValue.min.toFloat(), newValue.max.toFloat())
                        }

                        userInput = newValue
                        currentValue = newValue
                    }
                }

                override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                }

                override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {
                }
            })
        }
    }

    override fun onCurrentValueChanged(oldValue: RangePersistenceModel<Int>?, newValue: RangePersistenceModel<Int>?, byUser: Boolean) {
        if (!byUser) {
            newValue?.let {
                rangeSeekBar?.setValue(it.min.toFloat(), it.max.toFloat())
            }
        }
    }

}
