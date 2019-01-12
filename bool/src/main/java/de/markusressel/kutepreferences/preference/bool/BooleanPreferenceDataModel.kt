package de.markusressel.kutepreferences.preference.bool

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.CompoundButton
import de.markusressel.kutepreferences.core.viewmodel.base.PreferenceItemDataModel

/**
 * Data model for the boolean toggle preference
 */
class BooleanPreferenceDataModel(
        title: String,
        description: String,
        icon: Drawable?,
        val checked: Boolean,
        val onCheckedChange: CompoundButton.OnCheckedChangeListener,
        onClick: View.OnClickListener,
        onLongClick: View.OnLongClickListener)
    : PreferenceItemDataModel(title, description, icon = icon, onClick = onClick, onLongClick = onLongClick) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as BooleanPreferenceDataModel

        if (checked != other.checked) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + checked.hashCode()
        return result
    }
}