package de.markusressel.kutepreferences.preference.number.range

import de.markusressel.kutepreferences.core.view.edit.KutePreferenceEditDialogBase

abstract class KuteRangePreferenceEditDialog<InputType : Number>(
        override val preferenceItem: KuteRangePreference<InputType>,
        protected val minimum: InputType,
        protected val maximum: InputType) :
        KutePreferenceEditDialogBase<RangePersistenceModel<InputType>>()
