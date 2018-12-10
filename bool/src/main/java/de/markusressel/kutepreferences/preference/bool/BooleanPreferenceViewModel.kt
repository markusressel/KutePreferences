package de.markusressel.kutepreferences.preference.bool

import androidx.lifecycle.MutableLiveData
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.viewmodel.PreferenceItemViewModel

/**
 * ViewModel for the boolean toggle preference
 */
class BooleanPreferenceViewModel : PreferenceItemViewModel() {

    val checked = MutableLiveData<Boolean>()

    override fun highlight(highlighterFunction: HighlighterFunction) {
        TODO("not implemented")
    }

}