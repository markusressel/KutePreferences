package de.markusressel.kutepreferences.preference.color

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceBase

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
        get() = R.layout.kute_preference__color__list_item

    var colorPreviewView: View? = null

    override fun getDefaultValue(): Int = ContextCompat.getColor(context, defaultValue)

    override fun onLayoutInflated(layout: View) {
        super.onLayoutInflated(layout)
        colorPreviewView = layout.findViewById(R.id.kute_preferences__preference__color__preview)
    }

    override fun onClick(context: Context) {
        val dialog = KuteColorPreferenceEditDialog(this)
        dialog
                .show(context)
    }

    override fun updateDescription() {
        super.updateDescription()
        colorPreviewView?.setBackgroundColor(persistedValue)
    }

    override fun createDescription(currentValue: Int): String {
        val a = Color.alpha(currentValue).toString(16).padStart(2, '0')
        val r = Color.red(currentValue).toString(16).padStart(2, '0')
        val g = Color.green(currentValue).toString(16).padStart(2, '0')
        val b = Color.blue(currentValue).toString(16).padStart(2, '0')

        return "#$a$r$g$b"
    }

}