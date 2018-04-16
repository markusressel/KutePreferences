package de.markusressel.kutepreferences.library.preference

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.view.KutePreferenceView

class KuteTextPreference(override val key: Int,
                         override val name: String,
                         val minLength: Int? = null,
                         val maxLength: Int? = null,
                         val regex: String? = null,
                         override val defaultValue: String,
                         dataProvider: KutePreferenceDataProvider)
    : KutePreferenceBase<String>(dataProvider = dataProvider),
        KutePreferenceView,
        KutePreferenceOnClick<String> {

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__list_item__text, null, false) as ViewGroup

        val nameTextView: TextView = layout.findViewById(R.id.kute_preferences__preference__name)
        nameTextView.text = name

        val descriptionTextView: TextView = layout.findViewById(R.id.kute_preferences__preference__description)
        descriptionTextView.text = getDescription(getPersistedValue())

        return layout
    }

    override fun onClick(kutePreference: KutePreferenceItem<String>) {

    }

}