package de.markusressel.kutepreferences.preference.date

import android.graphics.drawable.Drawable
import android.view.View
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel

/**
 * Data model for the boolean toggle preference
 */
class DatePreferenceDataModel(
        title: String,
        description: String,
        icon: Drawable?,
        val date: Long,
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

        other as DatePreferenceDataModel

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}