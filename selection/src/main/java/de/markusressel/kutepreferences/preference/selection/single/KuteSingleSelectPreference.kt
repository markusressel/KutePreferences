package de.markusressel.kutepreferences.preference.selection.single

import android.content.Context
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialog
import de.markusressel.kutepreferences.preference.selection.R

/**
 * Base class for implementing a single selection preference
 *
 * @param T The preference item data type
 */
abstract class KuteSingleSelectPreference<T : Any> : KutePreferenceItem<T> {
    abstract val context: Context
    abstract val possibleValues: Map<Int, T>

    fun onClick(context: Context) {
        val dialog = createSingleSelectDialog(this, possibleValues)
        dialog
                .show(context)
    }

    /**
     * Creates an instance of an edit dialog for this preference item
     *
     * @return the dialog instance
     */
    internal abstract fun createSingleSelectDialog(kuteSingleSelectPreference: KuteSingleSelectPreference<T>,
                                                   possibleValues: Map<Int, T>): KutePreferenceEditDialog<T>

}