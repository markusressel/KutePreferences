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
    open fun isValid(): Boolean = validationError() == null

    /**
     * @return the reason why the [currentValue] cannot be saved, or null if it can be saved
     */
    fun validationError(): KuteValidationError? {
        val error = checkCurrentValue() ?: return null
        val message = preferenceItem.validationMessage?.invoke(currentValue.value)
        return when {
            message != null -> KuteValidationError.Message(message)
            else -> error
        }
    }

    /**
     * Override this to add rules to a behavior, in addition to the [KutePreferenceItem.validator].
     *
     * @return the reason why the [currentValue] cannot be saved, or null if it can be saved
     */
    protected open fun checkCurrentValue(): KuteValidationError? {
        return when {
            preferenceItem.validator?.invoke(currentValue.value) == false -> KuteValidationError.Invalid
            else -> null
        }
    }

}
