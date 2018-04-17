package de.markusressel.kutepreferences.library.preference

import android.content.Context

/**
 * Interface for Preferences
 */
interface KutePreferenceClickListener {

    /**
     * Called when a KutePreferenceListItem is clicked
     */
    fun onClick(context: Context)

}