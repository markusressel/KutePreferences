package de.markusressel.kutepreferences.core.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import de.markusressel.kutepreferences.core.R

class IconHelper {

    companion object {

        /**
         * Helper function to retrieve a list item icon.
         *
         * @param context application context
         * @param icon icon to use, or null
         *
         * @return icon if specified and not null, otherwise the default icon
         */
        fun getListItemIcon(context: Context, icon: Drawable? = null): Drawable {
            return icon ?: AppCompatResources.getDrawable(context, R.drawable.ic_settings_black_24dp)!!.apply {
                setTint(Color.RED)
            }
        }
    }

}
