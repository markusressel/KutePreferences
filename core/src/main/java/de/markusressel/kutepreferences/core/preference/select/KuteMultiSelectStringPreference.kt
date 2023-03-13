package de.markusressel.kutepreferences.core.preference.select

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider

/**
 * Implementation of a multi selection preference
 */
open class KuteMultiSelectStringPreference(
    val context: Context,
    override val key: Int,
    override val icon: Drawable? = null,
    override val title: String,
    @StringRes
    private val defaultValue: Set<Int>,
    override val possibleValues: Map<Int, Int>,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: Set<String>, newValue: Set<String>) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
) : KuteMultiSelectPreference<Int, String>() {

    override fun getDefaultValue(): Set<String> = defaultValue.asSequence().map { context.getString(it) }.toSet()

    override fun createDescription(currentValue: Set<String>): String {
        return possibleValues.filter {
            context.getString(it.key) in currentValue
        }.map {
            context.getString(it.value)
        }.joinToString(separator = ", ")
    }

}