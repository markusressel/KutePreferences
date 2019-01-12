package de.markusressel.kutepreferences.preference.text.url

import android.content.Context
import android.graphics.drawable.Drawable
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.preference.text.KuteTextPreference

/**
 * Implementation for a url preference
 */
open class KuteUrlPreference(context: Context,
                             key: Int,
                             icon: Drawable? = null,
                             title: String,
                             regex: String? = null,
                             defaultValue: String,
                             dataProvider: KutePreferenceDataProvider,
                             onPreferenceChangedListener: ((oldValue: String, newValue: String) -> Unit)? = null) :
        KuteTextPreference(
                context = context,
                key = key,
                icon = icon,
                title = title,
                regex = regex,
                defaultValue = defaultValue,
                dataProvider = dataProvider,
                onPreferenceChangedListener = onPreferenceChangedListener) {

    override fun onListItemClicked(context: Context) {
        KuteUrlPreferenceEditDialog(this, regex).show(context)
    }

}