package com.example.cryptobuzz.data.remote.dto

//List of Crypto price
data class CryptoPriceListingDTO (
    val success:Boolean,
    val terms:String,
    val privacy:String,
    val timestamp:Long,
    val target:String,
    val rates:Map<String,Double>
)