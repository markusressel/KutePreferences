package de.markusressel.kutepreferences.library.preference

import android.content.Context

/**
 * Interface for Preferences
 */
interface KutePreferenceClickListener {

    /**
     * Called when a KutePreferenceListItem is clicked
     * In most cases you want to open some kind of edit dialog in this method.
     */
    fun onClick(context: Context)

}