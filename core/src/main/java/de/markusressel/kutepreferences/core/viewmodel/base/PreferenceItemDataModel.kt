package de.markusressel.kutepreferences.core.viewmodel.base

import android.graphics.drawable.Drawable
import android.text.Spanned
import android.view.View

/**
 * Base class for the data model of a preference item's list entry
 */
open class PreferenceItemDataModel(
        /**
         * The title of the preference item
         */
        val title: Spanned,
        /**
         * The description of the preference item
         * This should always represent the current value of the preference item
         * if it is not visualized in some other form
         */
        val description: Spanned,

        /**
         * Indicates the visibility of the description TextView
         */
        val descriptionVisibility: Int = if (description.isBlank()) View.GONE else View.VISIBLE,

        /**
         * The icon of the preference item
         */
        val icon: Drawable?,

        /**
         * Action for clicks
         */
        val onClick: View.OnClickListener? = null,

        /**
         * Action for long clicks
         */
        var onLongClick: View.OnLongClickListener? = null

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PreferenceItemDataModel

        if (title != other.title) return false
        if (description != other.description) return false
        if (descriptionVisibility != other.descriptionVisibility) return false
        if (icon != other.icon) return false
        if (onClick != other.onClick) return false
        if (onLongClick != other.onLongClick) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + descriptionVisibility
        result = 31 * result + (icon?.hashCode() ?: 0)
        result = 31 * result + (onClick?.hashCode() ?: 0)
        result = 31 * result + (onLongClick?.hashCode() ?: 0)
        return result
    }
}