package de.markusressel.kutepreferences.preference.time

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.TimePicker
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialogBase

open class KuteTimePreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<TimePersistenceModel>) :
        KutePreferenceEditDialogBase<TimePersistenceModel>() {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__time__edit_dialog

    protected var timePickerView: TimePicker? = null

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        timePickerView = contentView
                .findViewById(R.id.kute_preferences__preference__time__time_picker_view)
        timePickerView?.setIs24HourView(true)

        userInput = persistedValue

        timePickerView?.let {
            currentValue?.let { current ->
                setCurrentTime(current.hourOfDay, current.minute)
            }

            it.setOnTimeChangedListener { view, hourOfDay, minute ->
                val newValue = TimePersistenceModel(hourOfDay, minute)

                userInput = newValue
                currentValue = newValue
            }
        }
    }

    override fun onCurrentValueChanged(oldValue: TimePersistenceModel?, newValue: TimePersistenceModel?, byUser: Boolean) {
        if (!byUser) {
            newValue?.let {
                setCurrentTime(it.hourOfDay, it.minute)
            }
        }
    }

    private fun setCurrentTime(hourOfDay: Int, minute: Int) {
        timePickerView?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.hour = hourOfDay
                it.minute = minute
            } else {
                it.currentHour = hourOfDay
                it.currentMinute = minute
            }
        }
    }

}