package de.markusressel.kutepreferences.core.preference.select

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider

/**
 * Implementation of a single selection preference for selecting simple string objects
 */
open class KuteSingleSelectStringPreference(
    val context: Context,
    override val key: Int,
    override val icon: Drawable? = null,
    override val title: String,
    @StringRes
    val defaultValue: Int,
    possibleValues: Map<Int, Int>,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
) : KuteSingleSelectPreference<String>() {

    override val possibleValues = possibleValues.entries.associate {
        it.key to context.getString(it.value)
    }

    override fun getDefaultValue(): String = context.getString(defaultValue)

    override fun createDescription(currentValue: String): String {
        return possibleValues.filter {
            context.getString(it.key) == currentValue
        }.map {
            it.value
        }.first()
    }

}
