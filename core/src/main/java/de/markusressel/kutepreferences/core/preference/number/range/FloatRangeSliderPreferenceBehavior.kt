package de.markusressel.kutepreferences.core.preference.number.range

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class FloatRangeSliderPreferenceBehavior(
    preferenceItem: KuteFloatRangePreference,
) : PersistedPreferenceBehavior<KuteFloatRangePreference, RangePersistenceModel<Float>>(
    preferenceItem
)