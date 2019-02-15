package de.markusressel.kutepreferences.core.preference

import android.content.Context

/**
 * Interface for Preferences
 */
interface KutePreferenceLongClickListener {

    /**
     * Called when a KutePreferenceListItem is long clicked
     *
     * @return true if the callback consumed the long click, false otherwise.
     */
    fun onListItemLongClicked(context: Context): Boolean

}