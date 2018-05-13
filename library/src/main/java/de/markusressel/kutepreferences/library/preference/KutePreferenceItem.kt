package de.markusressel.kutepreferences.library.preference

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.CallSuper
import android.support.annotation.CheckResult
import de.markusressel.kutepreferences.library.KutePreferenceListItem
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider

/**
 * Interface for Preferences
 */
interface KutePreferenceItem<DataType : Any> : KutePreferenceListItem, KutePreferenceLongClickListener {

    /**
     * Optional icon of this KutePreference
     */
    val icon: Drawable?

    /**
     * The title of this KutePreference
     */
    val title: String

    /**
     * The description of this KutePreference according to it's persisted value
     */
    val description: String
        get() = constructDescription(persistedValue)

    /**
     * Override this method if you want to provide a more sophisticated description
     *
     * @param currentValue the current value
     * @return the description for this preference according to the current value
     */
    @CheckResult
    fun constructDescription(currentValue: DataType): String {
        return "Value: '$currentValue'"
    }

    /**
     * The default value of this preference
     */
    val defaultValue: DataType

    /**
     * The persisted value of this preference
     */
    var persistedValue: DataType
        get() = dataProvider.getValue(this)
        set(newValue) {
            val oldValue = persistedValue
            if (oldValue != newValue) {
                dataProvider
                        .storeValue(this, newValue)
                onPreferenceChanged(oldValue, newValue)
            }
        }

    /**
     * Persistence for this PreferenceItem
     */
    val dataProvider: KutePreferenceDataProvider

    /**
     * Reset the current value (and persisted) value of this KutePreferenceListItem to the default value
     */
    @CallSuper
    fun reset() {
        persistedValue = defaultValue
    }

    /**
     * An optional listener for value changes
     */
    val onPreferenceChangedListener: ((oldValue: DataType, newValue: DataType) -> Unit)?

    /**
     * Called when the persisted value of this KutePreference changes it's value
     */
    @CallSuper
    fun onPreferenceChanged(oldValue: DataType, newValue: DataType) {
        onPreferenceChangedListener
                ?.invoke(oldValue, newValue)
    }

    override fun onLongClick(context: Context) {
        reset()
    }

}
