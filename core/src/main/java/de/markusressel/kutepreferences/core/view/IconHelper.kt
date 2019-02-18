package de.markusressel.kutepreferences.core.view

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import de.markusressel.commons.android.themes.getThemeAttrColor
import de.markusressel.kutepreferences.core.R
import java.lang.ref.WeakReference

class IconHelper {

    companion object {

        var contextRef: WeakReference<Context?> = WeakReference(null)

        /**
         * Helper function to retrieve a list item icon.
         *
         * @param context application context
         * @param icon icon to use, or null
         *
         * @return icon if specified and not null, otherwise the default icon
         */
        fun getListItemIcon(icon: Drawable? = null): Drawable? {
            val context = contextRef.get()
            return when (context) {
                null -> icon
                else -> icon ?: AppCompatResources.getDrawable(context, R.drawable.ic_settings_black_24dp)?.apply {
                    val defaultColor = context.getThemeAttrColor(R.attr.kute_preferences__setting__default_icon_color)
                    setTint(defaultColor)
                }
            }
        }
    }

}
