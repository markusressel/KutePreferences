package de.markusressel.kutepreferences.library.view

import de.markusressel.kutepreferences.library.preference.KutePreferenceItem

class KuteTextPreferenceEditDialog(
        override val preferenceItem: KutePreferenceItem<String>,
        override var currentValue: String?)
    : KutePreferenceEditDialog<String> {

    override fun show() {

    }

}