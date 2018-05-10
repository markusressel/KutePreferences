package de.markusressel.kutepreferences.library.preference.toggle

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener

class KuteTogglePreference(override val id: Long, override val key: Int, override val name: String, override val defaultValue: Boolean, override val dataProvider: KutePreferenceDataProvider, override val onPreferenceChangedListener: ((oldValue: Boolean, newValue: Boolean) -> Unit)? = null) : KutePreferenceBase<Boolean>(), KutePreferenceClickListener {

    override val layoutRes: Int
        get() = R.layout.kute_preference__list_item__toggle

    var descriptionTextView: TextView? = null
    var switchView: Switch? = null

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(layoutRes, null, false) as ViewGroup

        val nameTextView: TextView = layout
                .findViewById(R.id.kute_preferences__preference__name)
        nameTextView
                .text = name

        descriptionTextView = layout
                .findViewById(R.id.kute_preferences__preference__description)
        descriptionTextView
                ?.text = description

        switchView = layout
                .findViewById(R.id.kute_preferences__preference__toggle__switch)
        switchView
                ?.isChecked = persistedValue
        switchView
                ?.setOnCheckedChangeListener { _, newValue ->
                    persistedValue = newValue
                }

        return layout
    }

    override fun onClick(context: Context) {
        persistedValue = !persistedValue
    }

    override fun onPreferenceChanged(oldValue: Boolean, newValue: Boolean) {
        super
                .onPreferenceChanged(oldValue, newValue)

        // update description
        descriptionTextView
                ?.text = description
        // update switch state
        switchView
                ?.isChecked = newValue
    }

}