package de.markusressel.kutepreferences.preference.time

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem

open class KuteTimePreference(override val key: Int,
                              override val icon: Drawable? = null,
                              override val title: String,
                              private val defaultValue: TimePersistenceModel,
                              override val dataProvider: KutePreferenceDataProvider,
                              override val onPreferenceChangedListener: ((oldValue: TimePersistenceModel, newValue: TimePersistenceModel) -> Unit)? = null) :
        KutePreferenceItem<TimePersistenceModel> {


    override fun getDefaultValue(): TimePersistenceModel = defaultValue

    override fun createDescription(currentValue: TimePersistenceModel): String {
        return "${currentValue.hourOfDay}:${currentValue.minute}"
    }

}