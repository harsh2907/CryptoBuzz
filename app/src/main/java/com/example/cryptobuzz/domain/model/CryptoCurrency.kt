package com.example.cryptobuzz.domain.model

data class CryptoCurrency(
    val name:String,
    val symbol: String,
    val fullName: String,
    val price: String,
    val imageUrl: String
)