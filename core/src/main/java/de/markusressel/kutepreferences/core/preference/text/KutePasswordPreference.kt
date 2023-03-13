package de.markusressel.kutepreferences.core.preference.text

import android.graphics.drawable.Drawable
import androidx.annotation.CheckResult
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider

class KutePasswordPreference(
    override val key: Int,
    override val icon: Drawable? = null,
    override val title: String,
    regex: String? = "^.{8,}\$|^\$",
    defaultValue: String,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
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
) {

    @CheckResult
    override fun createDescription(currentValue: String): String {
        return '\u2022'.toString().repeat(currentValue.length)
    }

}