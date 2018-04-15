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
                         dataProvider: KutePreferenceDataProvider) : KutePreferenceBase<String>(dataProvider = dataProvider), KutePreferenceView {

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.list_item_kute_preference_text, null, false) as ViewGroup

        val nameTextView: TextView = layout.findViewById(R.id.name)
        nameTextView.text = name

        val descriptionTextView: TextView = layout.findViewById(R.id.description)
        descriptionTextView.text = getDescription(getPersistedValue())

        return layout
    }

    override fun getChildren(): List<KutePreferenceView> {
        return emptyList()
    }

}