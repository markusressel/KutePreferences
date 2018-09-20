package de.markusressel.kutepreferences.core.preference

import android.content.Context

/**
 * Interface for Preferences
 */
interface KutePreferenceLongClickListener {

    /**
     * Called when a KutePreferenceListItem is long clicked
     */
    fun onLongClick(context: Context)

}