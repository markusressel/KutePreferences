package de.markusressel.kutepreferences.library.preference

import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import de.markusressel.kutepreferences.library.R

/**
 * Base class for KutePreferenceListItem implementations
 */
abstract class KutePreferenceBase<DataType : Any> : KutePreferenceItem<DataType> {

    @get:LayoutRes
    abstract val layoutRes: Int

    private var iconView: ImageView? = null
    private var nameTextView: TextView? = null
    private var descriptionTextView: TextView? = null

    @CallSuper
    override fun inflateListLayout(layoutInflater: LayoutInflater): ViewGroup {
        val layout = layoutInflater.inflate(layoutRes, null, false) as ViewGroup

        iconView = layout
                .findViewById(R.id.kute_preference__preference__icon)
        iconView?.let {
            if (icon != null) {
                it.setImageDrawable(icon)
            } else {
                it.setImageResource(R.drawable.ic_settings_black_24dp)
                it.alpha = 0.5F
            }
        }

        nameTextView = layout
                .findViewById(R.id.kute_preferences__preference__title)
        nameTextView?.text = title

        descriptionTextView = layout
                .findViewById(R.id.kute_preferences__preference__description)
        descriptionTextView?.text = description

        return layout
    }

    @CallSuper
    override fun onPreferenceChanged(oldValue: DataType, newValue: DataType) {
        super
                .onPreferenceChanged(oldValue, newValue)

        descriptionTextView
                ?.text = description
    }

}