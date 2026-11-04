package de.markusressel.kutepreferences.core.preference.text

import android.graphics.drawable.Drawable
import androidx.annotation.CheckResult
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.Validator

class KutePasswordPreference(
    override val key: String,
    override val icon: Drawable? = null,
    override val title: String,
    regex: String? = "^.{8,}\$|^\$",
    defaultValue: String,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
    override val validator: Validator<String>? = null,
    override val visibleWhen: KuteCondition? = null,
    override val enabledWhen: KuteCondition? = null,
) : KuteTextPreference(
    key = key,
    icon = icon,
    title = title,
    regex = regex,
    defaultValue = defaultValue,
    dataProvider = dataProvider,
    onPreferenceChangedListener = onPreferenceChangedListener,
    onClick = onClick,
    onLongClick = onLongClick,
    validator = validator,
    visibleWhen = visibleWhen,
    enabledWhen = enabledWhen,
) {

    @CheckResult
    override fun createDescription(currentValue: String): String {
        return when {
            currentValue.isEmpty() -> ""
            // NOTE: the mask has a fixed length to not reveal the length of the password
            else -> '\u2022'.toString().repeat(MASK_LENGTH)
        }
    }

    private companion object {
        const val MASK_LENGTH = 8
    }

}