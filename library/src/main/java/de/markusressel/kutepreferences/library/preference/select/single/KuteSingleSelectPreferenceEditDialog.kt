package de.markusressel.kutepreferences.library.preference.select.single

import de.markusressel.kutepreferences.library.preference.KutePreferenceItem
import de.markusressel.kutepreferences.library.view.edit.KutePreferenceEditDialogBase

/**
 * Base class for an edit dialog of a single selection preference
 *
 * @param T The preference item data type
 */
abstract class KuteSingleSelectPreferenceEditDialog<T : Any>(
        override val preferenceItem: KutePreferenceItem<T>) :
        KutePreferenceEditDialogBase<T>() {

    /**
     * Map for easy lookup of values by it's key.
     * "value key" -> "value"
     */
    abstract val possibleValues: Map<Int, T>

}
