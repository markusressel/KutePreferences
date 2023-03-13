package de.markusressel.kutepreferencescomposeplayground.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.markusressel.kutepreferences.core.DefaultKuteNavigator
import de.markusressel.kutepreferences.core.KuteNavigator
import de.markusressel.kutepreferences.core.persistence.DefaultKutePreferenceDataProvider
import de.markusressel.kutepreferences.core.persistence.KutePreferenceDataProvider
import javax.inject.Singleton

/**
 * Created by Markus on 20.12.2017.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    internal fun provideDataProvider(context: Context): KutePreferenceDataProvider {
        return DefaultKutePreferenceDataProvider(context)
    }

    @Provides
    @Singleton
    internal fun provideKuteNavigator(): KuteNavigator {
        return DefaultKuteNavigator()
    }

}