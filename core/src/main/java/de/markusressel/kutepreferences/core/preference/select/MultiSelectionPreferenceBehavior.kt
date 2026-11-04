package de.markusressel.kutepreferences.core.preference.select

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior

open class MultiSelectionPreferenceBehavior(
    preferenceItem: KuteMultiSelectStringPreference
) : EditablePreferenceBehavior<KuteMultiSelectStringPreference, Set<String>>(
    preferenceItem
)
