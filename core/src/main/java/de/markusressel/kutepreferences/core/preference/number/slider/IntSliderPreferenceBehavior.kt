package de.markusressel.kutepreferences.core.preference.number.slider

import de.markusressel.kutepreferences.core.preference.EditablePreferenceBehavior

open class IntSliderPreferenceBehavior(
    preferenceItem: KuteIntSliderPreference
) : EditablePreferenceBehavior<KuteIntSliderPreference, Long>(
    preferenceItem
)
