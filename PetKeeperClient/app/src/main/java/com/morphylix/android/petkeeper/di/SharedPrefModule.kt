package com.morphylix.android.petkeeper.di

import android.content.Context
import com.morphylix.android.petkeeper.data.shared_pref.SharedPrefTokenStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPrefTokenStorage {
        return SharedPrefTokenStorage(context)
    }
}