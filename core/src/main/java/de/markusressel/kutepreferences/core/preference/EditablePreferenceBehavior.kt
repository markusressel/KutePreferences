package de.markusressel.kutepreferences.core.preference

/**
 * Behavior of a [KutePreferenceItem] whose value is edited before it is saved, f.ex. in a dialog.
 */
open class EditablePreferenceBehavior<T : KutePreferenceItem<K>, K : Any>(
    preferenceItem: T
) : PersistedPreferenceBehavior<T, K>(
    preferenceItem
) {

    fun onCancelClicked() = reset()

    fun onDefaultClicked() = restoreDefaultValue()

    /**
     * @return true if the [currentValue] can be saved
     */
    open fun isValid(): Boolean {
        return preferenceItem.validator?.invoke(currentValue.value) ?: true
    }

}
