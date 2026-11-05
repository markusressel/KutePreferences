package de.markusressel.kutepreferences.core.preference.select

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.Validator

/**
 * Multiple selections from the given [options], which can also be created at runtime.
 *
 * @param defaultValue values of the [options]
 * @param options the value to save for each option, mapped to the title to show for it
 */
open class KuteMultiChoicePreference(
    override val key: String,
    override val icon: Drawable? = null,
    override val title: String,
    private val defaultValue: Set<String> = emptySet(),
    override val options: Map<String, String>,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: Set<String>, newValue: Set<String>) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
    override val validator: Validator<Set<String>>? = null,
    override val visibleWhen: KuteCondition? = null,
    override val enabledWhen: KuteCondition? = null,
    override val validationMessage: ((Set<String>) -> String)? = null,
    override val keywords: List<String> = emptyList(),
) : KuteMultiSelectPreference<String, String>() {

    init {
        require(options.keys.containsAll(defaultValue)) { "The default value of \"$key\" is not part of its options" }
    }

    override fun getDefaultValue(): Set<String> = defaultValue

    override fun createDescription(currentValue: Set<String>): String {
        return options.filterKeys { it in currentValue }.values.joinToString(separator = ", ")
    }

}
