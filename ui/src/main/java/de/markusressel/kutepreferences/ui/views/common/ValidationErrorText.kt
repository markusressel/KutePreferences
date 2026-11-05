package de.markusressel.kutepreferences.ui.views.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior
import de.markusressel.kutepreferences.core.preference.KuteValidationError
import de.markusressel.kutepreferences.ui.R

/**
 * @return the text that explains why the current value of this behavior cannot be saved, if it can't
 */
@Composable
internal fun EditablePreferenceBehavior<*, *>.validationErrorText(): String? {
    // NOTE: dialogs treat an exception in a validator as invalid input
    val error = runCatching { validationError() }.getOrDefault(KuteValidationError.Invalid)
    return error?.let { validationErrorText(it) }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun validationErrorText(error: KuteValidationError): String {
    return when (error) {
        is KuteValidationError.Message -> error.text
        is KuteValidationError.Invalid -> stringResource(R.string.kute_preferences_error_invalid)
        is KuteValidationError.InvalidUrl -> stringResource(R.string.kute_preferences_error_invalid_url)
        is KuteValidationError.TooShort -> pluralStringResource(
            R.plurals.kute_preferences_error_too_short,
            error.minimumLength,
            error.minimumLength,
        )

        is KuteValidationError.OutOfRange -> outOfRangeText(error.minimum, error.maximum)
    }
}

@Composable
private fun outOfRangeText(minimum: Long?, maximum: Long?): String {
    return when {
        minimum != null && maximum != null -> {
            stringResource(R.string.kute_preferences_error_between, minimum, maximum)
        }

        minimum != null -> stringResource(R.string.kute_preferences_error_at_least, minimum)
        maximum != null -> stringResource(R.string.kute_preferences_error_at_most, maximum)
        else -> stringResource(R.string.kute_preferences_error_invalid)
    }
}
