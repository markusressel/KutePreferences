package de.markusressel.kutepreferences.library.preference

import android.content.Context
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
                           override val dataProvider: KutePreferenceDataProvider,
                           override val onPreferenceChangedListener: ((oldValue: Boolean, newValue: Boolean) -> Unit)? = null)
    : KutePreferenceBase<Boolean>(),
        KutePreferenceView,
        KutePreferenceClickListener {

    var descriptionTextView: TextView? = null
    var switchView: Switch? = null

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.kute_preference__list_item__toggle, null, false) as ViewGroup

        val nameTextView: TextView = layout.findViewById(R.id.kute_preferences__preference__name)
        nameTextView.text = name

        descriptionTextView = layout.findViewById(R.id.kute_preferences__preference__description)
        descriptionTextView?.text = description

        switchView = layout.findViewById(R.id.kute_preferences__preference__toggle__switch)
        switchView?.isChecked = persistedValue
        switchView?.setOnCheckedChangeListener { _, newValue ->
            persistedValue = newValue
        }

        return layout
    }

    override fun onClick(context: Context) {
        persistedValue = !persistedValue
    }

    override fun onPreferenceChanged(oldValue: Boolean, newValue: Boolean) {
        super.onPreferenceChanged(oldValue, newValue)

        // update description
        descriptionTextView?.text = description
        // update switch state
        switchView?.isChecked = newValue
    }

}