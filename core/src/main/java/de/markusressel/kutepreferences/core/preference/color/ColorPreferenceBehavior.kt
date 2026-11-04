package de.markusressel.kutepreferences.core.preference.color

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior

open class ColorPreferenceBehavior(
    preferenceItem: KuteColorPreference
) : EditablePreferenceBehavior<KuteColorPreference, Int>(
    preferenceItem
)
