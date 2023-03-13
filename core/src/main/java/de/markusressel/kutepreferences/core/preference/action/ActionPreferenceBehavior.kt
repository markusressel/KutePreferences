package de.markusressel.kutepreferences.core.preference.action

open class ActionPreferenceBehavior(
    val preferenceItem: KuteAction
) {

    fun onClick() {
        preferenceItem.onClick?.invoke()
    }

    fun onLongClick() {
        preferenceItem.onLongClick?.invoke()
    }

}