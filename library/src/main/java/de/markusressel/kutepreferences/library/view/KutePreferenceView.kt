package de.markusressel.kutepreferences.library.view

import android.view.LayoutInflater
import android.view.ViewGroup

interface KutePreferenceView {

    /**
     * Inflate the layout for this KutePreferenceListItem that will be visible in the overview list
     */
    fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup

}