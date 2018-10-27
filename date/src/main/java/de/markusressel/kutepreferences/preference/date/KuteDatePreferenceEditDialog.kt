package de.markusressel.kutepreferences.preference.date

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.CalendarView
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialogBase
import java.util.*

open class KuteDatePreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<Long>,
        private val minimum: Long?,
        private val maximum: Long?) :
        KutePreferenceEditDialogBase<Long>() {

    override val contentLayoutRes: Int
        get() = R.layout.kute_preference__date__edit_dialog

    var calendarView: CalendarView? = null

    override fun onContentViewCreated(context: Context, layoutInflater: LayoutInflater, contentView: View) {
        calendarView = contentView
                .findViewById(R.id.kute_preferences__preference__date__calendar_view)

        userInput = persistedValue

        calendarView?.let { c ->
            if (minimum != null) {
                c.minDate = minimum
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

    override fun onCurrentValueChanged(oldValue: Long?, newValue: Long?, byUser: Boolean) {
        if (!byUser) {
            newValue?.let {
                calendarView
                        ?.setDate(it, true, true)
            }
        }
    }

}
