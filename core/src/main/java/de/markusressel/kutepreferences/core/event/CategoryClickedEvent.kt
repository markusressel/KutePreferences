package de.markusressel.kutepreferences.core.event

import de.markusressel.kutepreferences.core.preference.category.KutePreferenceCategory

data class CategoryClickedEvent(val preferenceItem: KutePreferenceCategory)