package de.markusressel.kutepreferences.library.preference.date

import android.view.View
import android.widget.CalendarView
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.view.edit.KutePreferenceEditDialogBase

class KuteDatePreferenceEditDialog(override val preferenceItem: KutePreferenceItem<Long>) :
        KutePreferenceEditDialogBase<Long>() {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__date__edit_dialog

    var calendarView: CalendarView? = null

    var userInput: Long = 0

    override fun onContentViewCreated(contentView: View) {
        calendarView = contentView
                .findViewById(R.id.kute_preferences__preference__date__calendar_view)

        userInput = persistedValue

        calendarView?.let {
            it.minDate = 0
            it.maxDate = Long.MAX_VALUE

            it.setDate(persistedValue, true, true)

            it.setOnDateChangeListener { calendarView, year, month, dayofmonth ->
                val newValue = calendarView.date
                userInput = newValue
                currentValue = newValue
            }
        }
    }

    override fun onCurrentValueChanged(oldValue: Long?, newValue: Long?) {
        if (oldValue != newValue && newValue != userInput) {
            newValue?.let {
                calendarView
                        ?.setDate(it, true, true)
            }
        }
    }

}
