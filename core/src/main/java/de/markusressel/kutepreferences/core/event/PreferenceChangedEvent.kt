package de.markusressel.kutepreferences.core.event

import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

class PreferenceChangedEvent<T : Any>(val preferenceItem: KutePreferenceItem<T>, val oldValue: T, val newValue: T)