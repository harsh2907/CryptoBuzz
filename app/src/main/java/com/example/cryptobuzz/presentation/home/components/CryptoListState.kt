package com.example.cryptobuzz.presentation.home.components

import com.example.cryptobuzz.domain.model.CryptoCurrency

data class CryptoListState(
    val lastUpdated:Long = 0L,
    val cryptoList:List<CryptoCurrency> = emptyList(),
    val isLoading:Boolean = false,
    val error:String=  ""
)