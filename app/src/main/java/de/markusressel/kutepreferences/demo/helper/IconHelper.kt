package de.markusressel.kutepreferences.demo.helper

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.utils.color
import com.mikepenz.iconics.utils.paddingDp
import com.mikepenz.iconics.utils.sizeDp
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Simple Helper class for handling icons
 */
@Singleton
class IconHelper @Inject constructor(private val context: Context) {

    fun getIcon(icon: IIcon, @ColorInt color: Int = Color.GRAY, sizeDp: Int = 36,
                paddingDp: Int = 0): Drawable {
        return IconicsDrawable(context, icon).apply {
            this.sizeDp = sizeDp
            this.paddingDp = paddingDp
            this.color = IconicsColor.colorInt(color)
        }
    }

}