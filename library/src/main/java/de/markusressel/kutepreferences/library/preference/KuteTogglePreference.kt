package de.markusressel.kutepreferences.library.preference

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.view.KutePreferenceView

class KuteTogglePreference(override val key: Int,
                           override val name: String,
                           override val defaultValue: Boolean,
                           dataProvider: KutePreferenceDataProvider)
    : KutePreferenceBase<Boolean>(dataProvider = dataProvider),
        KutePreferenceView {

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__list_item__toggle, null, false) as ViewGroup

        val nameTextView: TextView = layout.findViewById(R.id.kute_preferences__preference__name)
        nameTextView.text = name

        val descriptionTextView: TextView = layout.findViewById(R.id.kute_preferences__preference__description)
        descriptionTextView.text = getDescription(getPersistedValue())

        val switchView: Switch = layout.findViewById(R.id.kute_preferences__preference__toggle__switch)
        switchView.isChecked = getPersistedValue()
        switchView.setOnCheckedChangeListener { _, newValue ->
            currentValue = newValue
            save()
        }

        return layout
    }

}