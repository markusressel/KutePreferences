package de.markusressel.kutepreferences.core.preference

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import de.markusressel.kutepreferences.core.KuteSearchProvider
import de.markusressel.kutepreferences.core.R

/**
 * Base class for KutePreferenceListItem implementations
 */
abstract class KutePreferenceBase<DataType : Any> : KutePreferenceItem<DataType>, KuteSearchProvider {

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected var iconView: ImageView? = null
    protected var nameTextView: TextView? = null
    protected var descriptionTextView: TextView? = null

    @CallSuper
    override fun inflateListLayout(layoutInflater: LayoutInflater, parent: ViewGroup): ViewGroup {
        val layout = layoutInflater.inflate(layoutRes, parent, false) as ViewGroup

        iconView = layout
                .findViewById(R.id.kute_preference__preference__icon)
        iconView?.let {
            if (icon != null) {
                it.setImageDrawable(icon)
            } else {
                it.setImageResource(R.drawable.ic_settings_black_24dp)
            }
        }

        nameTextView = layout
                .findViewById(R.id.kute_preferences__preference__title)
        nameTextView?.text = title

        descriptionTextView = layout
                .findViewById(R.id.kute_preferences__preference__description)

        onLayoutInflated(layout)

        updateDescription()

        return layout
    }

    /**
     * Do any additional layout work that you need to do here
     */
    protected open fun onLayoutInflated(layout: View) {
    }

    override fun onClick(context: Context) {
    }

    override fun onLongClick(context: Context) {
    }

    @CallSuper
    override fun onPreferenceChanged(oldValue: DataType, newValue: DataType) {
        super
                .onPreferenceChanged(oldValue, newValue)

        updateDescription()
    }

    open fun updateDescription() {
        descriptionTextView?.let {
            if (description.isBlank()) {
                it.visibility = View.GONE
            } else {
                it.visibility = View.VISIBLE
                it.text = description
            }
        }
    }

    override fun getSearchableItems(): Set<String> {
        return setOf(title, description)
    }

}