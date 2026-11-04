package de.markusressel.kutepreferences.core.preference.select

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior

open class SingleSelectionPreferenceBehavior(
    preferenceItem: KuteSingleSelectPreference<String>
) : EditablePreferenceBehavior<KuteSingleSelectPreference<String>, String>(
    preferenceItem
)
