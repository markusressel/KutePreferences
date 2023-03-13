package de.markusressel.kutepreferences.core.preference

import android.graphics.drawable.Drawable
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for Preferences
 */
interface KutePreferenceItem<DataType : Any> {

    /**
     * A unique identifier for this preference item
     */
    val key: Int

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
        get() = createDescription(persistedValue.value)

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

    val persistedValue: StateFlow<DataType>

    /**
     * The default value of this preference
     */
    fun getDefaultValue(): DataType

    fun getCurrentPersistedValue(): DataType = dataProvider.getValue(this)

    fun getPersistedValueFlow(): StateFlow<DataType> {
        return dataProvider.getValueFlow(this)
    }

    fun persistValue(newValue: DataType) {
        val oldValue = persistedValue.value
        if (oldValue != newValue) {
            dataProvider.storeValue(this, newValue)
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
        persistValue(getDefaultValue())
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
        onPreferenceChangedListener?.invoke(oldValue, newValue)
    }

}

/** Validator function, returns true if the input is valid, false otherwise */
typealias Validator<T> = (T) -> Boolean