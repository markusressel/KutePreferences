package de.markusressel.kutepreferences.library

import android.view.LayoutInflater
import android.view.ViewGroup
import de.markusressel.kutepreferences.library.view.KutePreferenceView


class SimpleKutePreferenceCategory(override val name: String, private val childViews: List<KutePreferenceView>) : KutePreferenceCategory, KutePreferenceView {

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        return layoutInflater.inflate(R.layout.kute_preference_category, null, false) as ViewGroup
    }

    override fun getChildren(): List<KutePreferenceView> {
        return childViews
    }

}
