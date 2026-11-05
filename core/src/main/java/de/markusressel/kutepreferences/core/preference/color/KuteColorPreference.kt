package de.markusressel.kutepreferences.core.preference.color

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KuteCondition
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.KuteSearchContent

/**
 * Preference item for a 4 channel color (rgba).
 * The color is stored as an integer.
 */
open class KuteColorPreference(
    override val key: String,
    override val icon: Drawable? = null,
    override val title: String,
    @field:ColorInt
    private val defaultValue: Int,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: Int, newValue: Int) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
    override val visibleWhen: KuteCondition? = null,
    override val enabledWhen: KuteCondition? = null,
    override val keywords: List<String> = emptyList(),
) : KutePreferenceItem<Int>, KutePreferenceListItem {

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun getDefaultValue(): Int = defaultValue

    override fun createDescription(currentValue: Int): String {
        return "#%08x".format(currentValue)
    }

    override val searchContent
        get() = KuteSearchContent(title = title, keywords = keywords, description = description)

}
