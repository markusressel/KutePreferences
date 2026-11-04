package de.markusressel.kutepreferences.core.preference.number.range

data class RangePersistenceModel<T : Number>(val min: T, val max: T)
