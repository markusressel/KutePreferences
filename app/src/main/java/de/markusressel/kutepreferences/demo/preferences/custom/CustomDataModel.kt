package de.markusressel.kutepreferences.demo.preferences.custom

import android.view.View

class CustomDataModel(
        val backgroundColor: Int,
        val onClick: View.OnClickListener?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomDataModel

        if (backgroundColor != other.backgroundColor) return false
        if (onClick != other.onClick) return false

        return true
    }

    override fun hashCode(): Int {
        var result = backgroundColor
        result = 31 * result + onClick.hashCode()
        return result
    }
}