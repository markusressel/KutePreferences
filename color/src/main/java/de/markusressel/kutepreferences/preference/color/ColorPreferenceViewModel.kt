package de.markusressel.kutepreferences.preference.color

import androidx.lifecycle.MutableLiveData
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.viewmodel.PreferenceItemViewModel

/**
 * ViewModel for the boolean toggle preference
 */
class ColorPreferenceViewModel : PreferenceItemViewModel() {

    val color = MutableLiveData<Int>()

    override fun highlight(highlighterFunction: HighlighterFunction) {
        TODO("not implemented")
    }

}