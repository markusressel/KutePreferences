package de.markusressel.kutepreferences.core.preference.select

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.Validator

/**
 * A single selection from the given [options], which can also be created at runtime.
 *
 * @param defaultValue the value of one of the [options]
 * @param options the value to save for each option, mapped to the title to show for it
 */
open class KuteChoicePreference(
    override val key: String,
    override val icon: Drawable? = null,
    override val title: String,
    private val defaultValue: String,
    override val options: Map<String, String>,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
    override val validator: Validator<String>? = null,
    override val visibleWhen: KuteCondition? = null,
    override val enabledWhen: KuteCondition? = null,
    override val validationMessage: ((String) -> String)? = null,
) : KuteSingleSelectPreference<String>() {

    init {
        require(defaultValue in options) { "The default value of \"$key\" is not one of its options" }
    }

    override fun getDefaultValue(): String = defaultValue

    override fun createDescription(currentValue: String): String {
        // the persisted value might not be part of the options anymore, f.ex. after an app update
        return options[currentValue] ?: currentValue
    }

}
