package de.markusressel.kutepreferences.core.preference.number.slider

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class FloatSliderPreferenceBehavior(
    preferenceItem: KuteFloatSliderPreference,
) : PersistedPreferenceBehavior<KuteFloatSliderPreference, Float>(
    preferenceItem
)