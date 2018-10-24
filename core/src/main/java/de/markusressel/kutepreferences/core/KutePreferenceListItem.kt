package de.markusressel.kutepreferences.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes

interface KutePreferenceListItem {

    /**
     * A unique identifier for this KutePreference
     */
    @get:StringRes
    val key: Int

    /**
     * Inflate the layout for this KutePreferenceListItem that will be visible in the overview list.
     *
     * NOTE: Remember to NOT attach your layout to the parent manually as this will be done for you in
     * [de.markusressel.kutepreferences.core.view.KutePreferencesMainFragment]
     *
     * @param layoutInflater a layout inflater that can be used to inflate your custom layout
     * @param parent the parent view to derive styles from
     */
    fun inflateListLayout(layoutInflater: LayoutInflater, parent: ViewGroup): ViewGroup

}
