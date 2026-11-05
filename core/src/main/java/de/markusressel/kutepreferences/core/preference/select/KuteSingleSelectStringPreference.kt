package de.markusressel.kutepreferences.core.preference.select

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.Validator

/**
 * Implementation of a single selection preference for selecting simple string objects
 */
open class KuteSingleSelectStringPreference(
    val context: Context,
    override val key: String,
    override val icon: Drawable? = null,
    override val title: String,
    @field:StringRes
    val defaultValue: Int,
    possibleValues: Map<Int, Int>,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
    override val validator: Validator<String>? = null,
    override val visibleWhen: KuteCondition? = null,
    override val enabledWhen: KuteCondition? = null,
    override val validationMessage: ((String) -> String)? = null,
    override val keywords: List<String> = emptyList(),
) : KuteSingleSelectPreference<String>() {

    override val possibleValues = possibleValues.entries.associate {
        it.key to context.getString(it.value)
    }

    override val options: Map<String, String>
        get() = this.possibleValues.entries.associate { context.getString(it.key) to it.value }

    override fun getDefaultValue(): String = context.getString(defaultValue)

    override fun createDescription(currentValue: String): String {
        // the persisted value might not be part of the possible values anymore, f.ex. after an app update
        return possibleValues.entries.firstOrNull {
            context.getString(it.key) == currentValue
        }?.value ?: currentValue
    }

}
