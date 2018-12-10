package de.markusressel.kutepreferences.preference.date

import androidx.lifecycle.MutableLiveData
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.viewmodel.PreferenceItemViewModel

/**
 * ViewModel for a date preference
 */
class DatePreferenceViewModel : PreferenceItemViewModel() {

    val date = MutableLiveData<Long>()

    override fun highlight(highlighterFunction: HighlighterFunction) {
        TODO("not implemented")
    }

}