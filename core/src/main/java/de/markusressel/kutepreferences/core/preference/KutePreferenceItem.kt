package de.markusressel.kutepreferences.core.preference

import android.graphics.drawable.Drawable
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import de.markusressel.kutepreferences.core.KutePreferenceListItem
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider

/**
 * Interface for Preferences
 */
interface KutePreferenceItem<DataType : Any> : KutePreferenceListItem, KutePreferenceClickListener,
        KutePreferenceLongClickListener {

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
        get() = createDescription(persistedValue)

    /**
     * Override this method if you want to provide a more sophisticated description.
     *
     * Note: Remember that according to material design guidelines the description should always
     * reflect the currently saved value of a preference.
     *
     * @param currentValue the current value
     * @return the description for this preference according to the current value
     */
    @CheckResult
    fun createDescription(currentValue: DataType): String {
        return "$currentValue"
    }

    /**
     * The default value of this preference
     */
    fun getDefaultValue(): DataType

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
        persistedValue = getDefaultValue()
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

}
