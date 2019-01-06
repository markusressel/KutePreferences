package de.markusressel.kutepreferences.core

import android.content.Context
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.preference.KutePreferenceClickListener
import de.markusressel.kutepreferences.core.preference.KutePreferenceLongClickListener

interface KutePreferenceListItem : KutePreferenceClickListener, KutePreferenceLongClickListener {

    /**
     * A unique identifier for this KutePreference
     */
    @get:StringRes
    val key: Int

    /**
     * Returns an instance of an epoxy viewmodel for this KutePreferenceListItem
     */
    fun createEpoxyModel(): EpoxyModel<*>

    override fun onListItemClicked(context: Context) {
    }

    override fun onListItemLongClicked(context: Context): Boolean {
        return false
    }

}
