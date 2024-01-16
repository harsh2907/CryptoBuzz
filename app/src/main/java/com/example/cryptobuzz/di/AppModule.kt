package com.example.cryptobuzz.di

import com.example.cryptobuzz.data.remote.CryptoApi
import com.example.cryptobuzz.data.repository.CryptoRepositoryImpl
import com.example.cryptobuzz.domain.repository.CryptoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideCryptoApi(
        client: OkHttpClient
    ):CryptoApi{
        return Retrofit.Builder()
            .baseUrl(CryptoApi.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CryptoApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCryptoRepository(cryptoApi: CryptoApi):CryptoRepository{
        return CryptoRepositoryImpl(cryptoApi)
    }

}