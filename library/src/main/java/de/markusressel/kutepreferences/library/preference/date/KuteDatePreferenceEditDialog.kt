package de.markusressel.kutepreferences.library.preference.date

import android.view.View
import android.widget.CalendarView
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.view.edit.KutePreferenceEditDialogBase
import java.util.*

class KuteDatePreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<Long>,
        val mininum: Long?, val maximum: Long?) :
        KutePreferenceEditDialogBase<Long>() {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__date__edit_dialog

    var calendarView: CalendarView? = null

    var userInput: Long = 0

    override fun onContentViewCreated(contentView: View) {
        calendarView = contentView
                .findViewById(R.id.kute_preferences__preference__date__calendar_view)

        userInput = persistedValue

        calendarView?.let { c ->
            if (mininum != null) {
                c.minDate = mininum
            } else {
                c.minDate = 0
            }

            if (maximum != null) {
                c.maxDate = maximum
            } else {
                c.maxDate = Long.MAX_VALUE
            }

            c.setDate(persistedValue, true, true)

            c.setOnDateChangeListener { calendarView, year, month, dayOfMonth ->
                val calendar = GregorianCalendar(year, month, dayOfMonth)
                val newValue = calendar.timeInMillis
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
