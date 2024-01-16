package com.example.cryptobuzz.data.remote

import com.example.cryptobuzz.data.remote.dto.CryptoPriceListingDTO
import com.example.cryptobuzz.data.remote.dto.CryptoDetailsListingDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApi {

    @GET("live")
    suspend fun getCryptoPriceListing(
        @Query("access_key") apiKey:String = API_KEY,
        @Query("target") currency:String = CURRENCY
    ):CryptoPriceListingDTO


    @GET("list")
    suspend fun getCryptoDetailsListing(
        @Query("access_key") apiKey:String = API_KEY
    ):CryptoDetailsListingDTO



    companion object{
        const val API_KEY = "c267f71dc1172cb0657027a42259217f"
        const val CURRENCY = "INR"
        const val BASE_URL = "http://api.coinlayer.com/"
    }
}