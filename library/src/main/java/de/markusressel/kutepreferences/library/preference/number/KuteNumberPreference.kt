package de.markusressel.kutepreferences.library.preference.number

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import de.markusressel.kutepreferences.library.R
import de.markusressel.kutepreferences.library.persistence.KutePreferenceDataProvider
import de.markusressel.kutepreferences.library.preference.KutePreferenceBase
import de.markusressel.kutepreferences.library.preference.KutePreferenceClickListener

class KuteNumberPreference(override val key: Int,
                           override val icon: Drawable? = null,
                           override val title: String,
                           val mininum: Int? = null,
                           val maximum: Int? = null,
                           override val defaultValue: Long,
                           val unit: String? = null,
                           override val dataProvider: KutePreferenceDataProvider,
                           override val onPreferenceChangedListener: ((oldValue: Long, newValue: Long) -> Unit)? = null) :
        KutePreferenceBase<Long>(), KutePreferenceClickListener {

    override val layoutRes: Int
        get() = R.layout.kute_preference__number__list_item

    private var descriptionTextView: TextView? = null

    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(layoutRes, null, false) as ViewGroup

        val iconView: ImageView = layout
                .findViewById(R.id.kute_preference__preference__icon)
        if (icon != null) {
            iconView.setImageDrawable(icon)
        } else {
//            iconView.visibility = View.GONE
        }

        val nameTextView: TextView = layout
                .findViewById(R.id.kute_preferences__preference__title)
        nameTextView
                .text = title

        descriptionTextView = layout
                .findViewById(R.id.kute_preferences__preference__description)
        descriptionTextView
                ?.text = description

        return layout
    }

    override fun constructDescription(currentValue: Long): String {
        unit?.let {
            return "$currentValue $it"
        }

        return "$currentValue"
    }

    override fun onClick(context: Context) {
        val dialog = KuteNumberPreferenceEditDialog(this)
        dialog
                .show(context)
    }

    override fun onPreferenceChanged(oldValue: Long, newValue: Long) {
        super
                .onPreferenceChanged(oldValue, newValue)

        descriptionTextView
                ?.text = description
    }

}