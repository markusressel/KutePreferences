package de.markusressel.kutepreferences.library.preference.toggle

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener

open class KuteTogglePreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        override val defaultValue: Boolean,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Boolean, newValue: Boolean) -> Unit)? = null) :
        KutePreferenceBase<Boolean>(), KutePreferenceClickListener {

    override val layoutRes: Int
        get() = R.layout.kute_preference__toggle__list_item

    private var switchView: Switch? = null

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = super.inflateListLayout(layoutInflater)

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

        // update switch state
        switchView
                ?.isChecked = newValue
    }

}