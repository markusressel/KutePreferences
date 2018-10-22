package de.markusressel.kutepreferences.library.ktx

import android.content.Context
import android.widget.Toast

/**
 * Note that this is discouraged, as toast do not show when notifications are disabled for an app!
 *
 * Shorthand way of displaying a toast
 */
@Deprecated(message = "The use of toasts is discouraged as they DO NOT SHOW when notifications are disabled for an app.")
fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast =
        Toast.makeText(this.applicationContext, text, duration).apply { show() }