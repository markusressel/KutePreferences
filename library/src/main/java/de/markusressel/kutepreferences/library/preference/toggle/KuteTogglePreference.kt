package de.markusressel.kutepreferences.library.preference.toggle

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase

/**
 * Implementation of a boolean preference
 */
open class KuteTogglePreference(
        override val key: Int,
        override val icon: Drawable? = null,
        override val title: String,
        val descriptionFunction: ((Boolean) -> String)? = null,
        private val defaultValue: Boolean,
        override val dataProvider: KutePreferenceDataProvider,
        override val onPreferenceChangedListener: ((oldValue: Boolean, newValue: Boolean) -> Unit)? = null) :
        KutePreferenceBase<Boolean>() {

    override val layoutRes: Int
        get() = R.layout.kute_preference__toggle__list_item

    override fun getDefaultValue(): Boolean = defaultValue

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

    override fun createDescription(currentValue: Boolean): String {
        descriptionFunction?.let {
            return it(currentValue)
        }

        // if no specific description is given
        // there is no additional value in a "true" or "false" description
        // since it is already visible on the toggle
        return ""
    }

}