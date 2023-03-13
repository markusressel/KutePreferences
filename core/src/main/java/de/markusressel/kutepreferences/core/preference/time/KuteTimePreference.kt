package de.markusressel.kutepreferences.core.preference.time

import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.core.preference.KutePreferenceItem
import de.markusressel.kutepreferences.core.preference.KutePreferenceListItem
import de.markusressel.kutepreferences.core.search.SearchUtils.containsAnyWord
import java.text.DecimalFormat

open class KuteTimePreference(
    override val key: Int,
    override val icon: Drawable? = null,
    override val title: String,
    private val defaultValue: TimePersistenceModel,
    override val dataProvider: KutePreferenceDataProvider,
    override val onPreferenceChangedListener: ((oldValue: TimePersistenceModel, newValue: TimePersistenceModel) -> Unit)? = null,
    override val onClick: (() -> Unit)? = null,
    override val onLongClick: (() -> Unit)? = null,
) : KutePreferenceItem<TimePersistenceModel>, KutePreferenceListItem {

    override val persistedValue by lazy { getPersistedValueFlow() }

    override fun getDefaultValue(): TimePersistenceModel = defaultValue

    override fun createDescription(currentValue: TimePersistenceModel): String {
        return "${HOUR_FORMAT.format(currentValue.hourOfDay)}:${MINUTE_FORMAT.format(currentValue.minute)}"
    }

    override fun search(searchTerm: String) =
        listOf(title, description).containsAnyWord(searchTerm)

    private companion object {
        val HOUR_FORMAT = DecimalFormat("00")
        val MINUTE_FORMAT = DecimalFormat("00")
    }

}