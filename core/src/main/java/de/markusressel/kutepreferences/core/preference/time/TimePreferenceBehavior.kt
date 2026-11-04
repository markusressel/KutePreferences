package de.markusressel.kutepreferences.core.preference.time

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior

open class TimePreferenceBehavior(
    preferenceItem: KuteTimePreference
) : EditablePreferenceBehavior<KuteTimePreference, TimePersistenceModel>(
    preferenceItem
)
