package de.markusressel.kutepreferences.core.preference.color

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.compose.ui.graphics.Color
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord

/**
 * Preference item for a 4 channel color (rgba).
 * The color is stored as an integer.
 */
open class KuteColorPreference(
    override val key: Int,
    override val icon: Drawable? = null,
    override val title: String,
    @ColorInt
    private val defaultValue: Int,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: Int, newValue: Int) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
) : KutePreferenceItem<Int>, KutePreferenceListItem {

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun getDefaultValue(): Int = defaultValue

    override fun createDescription(currentValue: Int): String {
        val color = Color(currentValue)
        return color.toHexString()
    }

    override fun search(searchTerm: String) =
        listOf(title, description).containsAnyWord(searchTerm)

}

private fun Color.toHexString(): String {
    val a = (alpha * 255).toInt().toString(16).padStart(2, '0')
    val r = (red * 255).toInt().toString(16).padStart(2, '0')
    val g = (green * 255).toInt().toString(16).padStart(2, '0')
    val b = (blue * 255).toInt().toString(16).padStart(2, '0')

    return "#$a$r$g$b"
}
