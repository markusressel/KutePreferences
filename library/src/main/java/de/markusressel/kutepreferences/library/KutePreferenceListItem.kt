package de.markusressel.kutepreferences.library

import android.view.LayoutInflater
import android.view.ViewGroup

interface KutePreferenceListItem {

    /**
     * Inflate the layout for this KutePreferenceListItem that will be visible in the overview list
     */
    fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup

}
