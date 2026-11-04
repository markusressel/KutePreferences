package de.markusressel.kutepreferences.core.preference.select

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior

open class MultiSelectionPreferenceBehavior(
    preferenceItem: KuteMultiSelectPreference<*, String>
) : EditablePreferenceBehavior<KuteMultiSelectPreference<*, String>, Set<String>>(
    preferenceItem
)
