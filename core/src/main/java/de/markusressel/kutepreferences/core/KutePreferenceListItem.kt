package de.markusressel.kutepreferences.core

import android.content.Context
import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyModel
import de.markusressel.kutepreferences.core.preference.KutePreferenceClickListener
import de.markusressel.kutepreferences.core.preference.KutePreferenceLongClickListener

interface KutePreferenceListItem : KuteSearchable, KutePreferenceClickListener, KutePreferenceLongClickListener {

    /**
     * A unique identifier for this [KutePreferenceListItem]
     */
    @get:StringRes
    val key: Int

    /**
     * Creates the epoxy viewmodel for this [KutePreferenceListItem]
     *
     * @param highlighterFunction the function used to highlight items based on the current search text
     * @return the viewmodel representing the current state of this [KutePreferenceListItem]
     */
    fun createEpoxyModel(highlighterFunction: HighlighterFunction): EpoxyModel<*>

    override fun onListItemClicked(context: Context) {
    }

    override fun onListItemLongClicked(context: Context): Boolean {
        return false
    }

}
