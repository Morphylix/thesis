package com.morphylix.android.petkeeper.di

import com.google.gson.Gson
import com.morphylix.android.petkeeper.data.api.PetKeeperApi
import com.morphylix.android.petkeeper.domain.model.network.pet.PetNetworkEntity
import com.morphylix.android.petkeeper.util.BASE_URL_AS
import com.morphylix.android.petkeeper.util.BASE_URL_MOBILE
import com.morphylix.android.petkeeper.util.DateDeserializer
import com.morphylix.android.petkeeper.util.serializers.PetDeserializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.*


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson().newBuilder()
//            .registerTypeAdapter(PetNetworkEntity::class.java, PetDeserializer())
            .registerTypeAdapter(Date::class.java, DateDeserializer()).create()
    }

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(logging).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_AS) // Replace with BASE_URL_MOBILE for real phone
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun providePetKeeperApi(retrofit: Retrofit): PetKeeperApi {
        return retrofit.create(PetKeeperApi::class.java)
    }


}