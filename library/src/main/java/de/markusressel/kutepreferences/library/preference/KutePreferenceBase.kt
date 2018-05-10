package de.markusressel.kutepreferences.library.preference

import android.support.annotation.LayoutRes

/**
 * Base class for KutePreferenceListItem implementations
 */
abstract class KutePreferenceBase<DataType : Any> : KutePreferenceItem<DataType> {

    @get:LayoutRes
    abstract val layoutRes: Int

}