package de.markusressel.kutepreferences.core.viewmodel

import android.view.View

class SectionViewModel(
        /**
         * The title of the section
         */
        val title: String,

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

        other as SectionViewModel

        if (title != other.title) return false
        if (onClick != other.onClick) return false
        if (onLongClick != other.onLongClick) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + (onClick?.hashCode() ?: 0)
        result = 31 * result + (onLongClick?.hashCode() ?: 0)
        return result
    }
}