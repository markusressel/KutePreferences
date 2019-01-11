package de.markusressel.kutepreferences.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SectionViewModel : ViewModel() {

    /**
     * The title of the preference item
     */
    val title = MutableLiveData<String>()

}