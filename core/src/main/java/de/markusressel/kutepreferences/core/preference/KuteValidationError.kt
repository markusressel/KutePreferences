package de.markusressel.kutepreferences.core.preference

/**
 * The reason why the current value of an [EditablePreferenceBehavior] cannot be saved.
 */
sealed class KuteValidationError {

    /**
     * A text from [KutePreferenceItem.validationMessage]
     */
    data class Message(val text: String) : KuteValidationError()

    /**
     * The value was rejected, and there is no more specific reason
     */
    data object Invalid : KuteValidationError()

    data object InvalidUrl : KuteValidationError()

    data class TooShort(val minimumLength: Int) : KuteValidationError()

    /**
     * @param minimum the smallest allowed value, if there is one
     * @param maximum the largest allowed value, if there is one
     */
    data class OutOfRange(val minimum: Long?, val maximum: Long?) : KuteValidationError()

}
