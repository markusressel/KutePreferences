package de.markusressel.kutepreferences.demo.preferences.custom

class CustomDataModel(
        val backgroundColor: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CustomDataModel

        if (backgroundColor != other.backgroundColor) return false

        return true
    }

    override fun hashCode(): Int {
        return backgroundColor.hashCode()
    }
}