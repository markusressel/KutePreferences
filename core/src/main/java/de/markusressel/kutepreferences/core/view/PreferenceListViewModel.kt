package de.markusressel.kutepreferences.core.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

class PreferenceListViewModel : ViewModel() {

    val preferencesLiveData = MutableLiveData<List<KutePreferenceItem<*>>>()

}