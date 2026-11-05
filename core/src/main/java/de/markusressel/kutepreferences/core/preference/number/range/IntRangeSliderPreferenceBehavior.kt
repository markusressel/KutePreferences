package de.markusressel.kutepreferences.core.preference.number.range

import de.markusressel.kutepreferences.core.preference.PersistedPreferenceBehavior

open class IntRangeSliderPreferenceBehavior(
    preferenceItem: KuteIntRangePreference,
) : PersistedPreferenceBehavior<KuteIntRangePreference, RangePersistenceModel<Long>>(
    preferenceItem
)
