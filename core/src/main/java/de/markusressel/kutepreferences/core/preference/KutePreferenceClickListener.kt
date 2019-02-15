package de.markusressel.kutepreferences.core.preference

import android.content.Context

/**
 * Interface for Preferences
 */
interface KutePreferenceClickListener {

    /**
     * Called when a KutePreferenceListItem is clicked
     * In most cases you want to open some kind of edit dialog in this method.
     */
    fun onListItemClicked(context: Context)

}