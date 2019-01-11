package de.markusressel.kutepreferences.preference.text

import androidx.lifecycle.MutableLiveData
import de.markusressel.kutepreferences.core.HighlighterFunction
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemViewModel

/**
 * ViewModel for the text preferences
 */
class TextPreferenceViewModel : PreferenceItemViewModel() {

    val text = MutableLiveData<String>()

    override fun highlight(highlighterFunction: HighlighterFunction) {
        TODO("not implemented")
    }

}