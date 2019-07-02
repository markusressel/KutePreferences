package de.markusressel.kutepreferences.core

import androidx.annotation.StringRes
import com.airbnb.epoxy.EpoxyModel

interface KutePreferenceListItem : KuteSearchable {

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

}
