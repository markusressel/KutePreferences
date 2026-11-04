package de.markusressel.kutepreferences.core.preference.select

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior

open class SingleSelectionPreferenceBehavior(
    preferenceItem: KuteSingleSelectStringPreference
) : EditablePreferenceBehavior<KuteSingleSelectStringPreference, String>(
    preferenceItem
)
