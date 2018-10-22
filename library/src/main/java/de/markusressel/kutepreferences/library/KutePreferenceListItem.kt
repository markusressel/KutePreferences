package de.markusressel.kutepreferences.library

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
     * Inflate the layout for this KutePreferenceListItem that will be visible in the overview list
     */
    fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup

}
