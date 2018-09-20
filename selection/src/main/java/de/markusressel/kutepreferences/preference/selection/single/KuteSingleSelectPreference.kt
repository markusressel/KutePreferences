package de.markusressel.kutepreferences.preference.selection.single

import android.content.Context
import de.markusressel.kutepreferences.core.preference.KutePreferenceBase
import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialog
import de.markusressel.kutepreferences.preference.R

/**
 * Base class for implementing a single selection preference
 *
 * @param T The preference item data type
 */
abstract class KuteSingleSelectPreference<T : Any> :
        KutePreferenceBase<T>() {
    abstract val context: Context
    abstract val possibleValues: Map<Int, T>

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun onClick(context: Context) {
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