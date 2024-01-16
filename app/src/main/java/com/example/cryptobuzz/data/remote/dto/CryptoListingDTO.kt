package com.example.cryptobuzz.data.remote.dto

import com.google.gson.annotations.SerializedName

//List of Crypto with details
data class CryptoDetailsListingDTO(
    val success: Boolean,
    @SerializedName("crypto")
    val cryptoList: Map<String, CryptoCurrencyDTO>
)

data class CryptoCurrencyDTO(
    val symbol: String,
    val name: String,
    @SerializedName("name_full")
    val fullName: String,
    @SerializedName("icon_url")
    val iconUrl: String
)