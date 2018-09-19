package de.markusressel.kutepreferences.library.preference.color

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase

/**
 * Preference item for a 4 channel color (rgba).
 * The color is stored as an integer.
 */
open class KuteColorPreference(
        private val context: Context,
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        @ColorRes
        private val defaultValue: Int,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Int, newValue: Int) -> Unit)? = null) :
        KutePreferenceBase<Int>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__default__list_item

    override fun getDefaultValue(): Int = ContextCompat.getColor(context, defaultValue)

    override fun onClick(context: Context) {
        val dialog = KuteColorPreferenceEditDialog(this)
        dialog
                .show(context)
    }

    override fun createDescription(currentValue: Int): String {
        val r = (currentValue shr 16 and 0xff) / 255.0f
        val g = (currentValue shr 8 and 0xff) / 255.0f
        val b = (currentValue and 0xff) / 255.0f
        val a = (currentValue shr 24 and 0xff) / 255.0f

        return "#$a$r$g$b"
    }

}