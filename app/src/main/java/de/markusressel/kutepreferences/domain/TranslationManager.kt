package de.markusressel.kutepreferences.domain

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TranslationManager @Inject constructor(
    private val context: Context,
) {
    fun getTranslation(@StringRes resourceId: Int): String {
        return context.getString(resourceId)
    }
}