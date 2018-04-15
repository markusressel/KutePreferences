package de.markusressel.kutepreferences.library.view

import android.view.LayoutInflater
import android.view.ViewGroup

interface KutePreferenceView {

    /**
     * Inflate the layout for this KutePreference that will be visible in the overview list
     */
    fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup

    /**
     * @return the list of child views of this view
     */
    fun getChildren(): List<KutePreferenceView>

}