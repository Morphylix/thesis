package com.morphylix.android.petkeeper.di

import com.morphylix.android.petkeeper.data.PetKeeperRepositoryImpl
import com.morphylix.android.petkeeper.data.api.PetKeeperApi
import com.morphylix.android.petkeeper.data.shared_pref.SharedPrefTokenStorage
import com.morphylix.android.petkeeper.domain.PetKeeperRepository
import com.morphylix.android.petkeeper.domain.model.network.NetworkMappers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        petKeeperApi: PetKeeperApi,
        sharedPrefTokenStorage: SharedPrefTokenStorage,
        networkMappers: NetworkMappers
    ): PetKeeperRepository {
        return PetKeeperRepositoryImpl(petKeeperApi, sharedPrefTokenStorage, networkMappers)
    }
}