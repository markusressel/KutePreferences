package de.markusressel.kutepreferences.preference.color

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel

/**
 * Data model for the boolean toggle preference
 */
class ColorPreferenceDataModel(
        title: String,
        description: String,
        icon: Drawable?,
        @ColorInt
        val color: Int,
        onClick: View.OnClickListener,
        onLongClick: View.OnLongClickListener
) : PreferenceItemDataModel(
        title = title,
        description = description,
        icon = icon,
        onClick = onClick,
        onLongClick = onLongClick
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ColorPreferenceDataModel

        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + color
        return result
    }
}