package de.markusressel.kutepreferences.library.preference

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.view.KutePreferenceView
import de.markusressel.kutepreferences.library.view.KuteTextPreferenceEditDialog

class KuteTextPreference(override val key: Int,
                         override val name: String,
                         val minLength: Int? = null,
                         val maxLength: Int? = null,
                         val regex: String? = null,
                         override val defaultValue: String,
                         override val dataProvider: KutePreferenceDataProvider,
                         override val onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null)
    : KutePreferenceBase<String>(),
        KutePreferenceView,
        KutePreferenceClickListener {

    var descriptionTextView: TextView? = null

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__list_item__text, null, false) as ViewGroup

        val nameTextView: TextView = layout.findViewById(R.id.kute_preferences__preference__name)
        nameTextView.text = name

        descriptionTextView = layout.findViewById(R.id.kute_preferences__preference__description)
        descriptionTextView?.text = description

        return layout
    }

    override fun onClick(context: Context) {
        // TODO: show edit dialog
        KuteTextPreferenceEditDialog(this, persistedValue)
    }

    override fun updateDescription() {
        descriptionTextView?.text = description
    }

}