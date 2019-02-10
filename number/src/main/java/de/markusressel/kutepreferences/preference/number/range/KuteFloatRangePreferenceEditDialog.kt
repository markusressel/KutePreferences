package de.markusressel.kutepreferences.preference.number.range

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import de.markusressel.kutepreferences.preference.number.R

open class KuteFloatRangePreferenceEditDialog(
        preferenceItem: KuteRangePreference<Float>,
        minimum: Float,
        maximum: Float,
        private val decimalPlaces: Int) :
        KuteRangePreferenceEditDialog<Float>(
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
            val decimalFormat = when {
                decimalPlaces <= 0 -> "0"
                else -> "0.${"0".repeat(decimalPlaces)}"
            }
            it.setIndicatorTextDecimalFormat(decimalFormat)

            val stepSize = (maximum - minimum) / 10
            it.tickMarkTextArray = (0..10).mapIndexed { index, item ->
                minimum + stepSize * index
            }.map {
                "%.${decimalPlaces}f".format(it)
            }.toTypedArray()

            it.setRange(minimum, maximum)
            it.setValue(persistedValue.min, persistedValue.max)

            it.setOnRangeChangedListener(object : OnRangeChangedListener {
                override fun onRangeChanged(rangeSeekBar1: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                    rangeSeekBar1?.let {
                        val newValue = RangePersistenceModel(leftValue, rightValue)
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

    override fun onCurrentValueChanged(oldValue: RangePersistenceModel<Float>?, newValue: RangePersistenceModel<Float>?, byUser: Boolean) {
        if (!byUser) {
            newValue?.let {
                rangeSeekBar?.setValue(it.min, it.max)
            }
        }
    }

}
