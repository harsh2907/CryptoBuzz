package com.example.cryptobuzz.domain.repository

import com.example.cryptobuzz.data.remote.dto.CryptoPriceListingDTO
import com.example.cryptobuzz.data.remote.dto.CryptoDetailsListingDTO
import com.example.cryptobuzz.data.utils.Response
import kotlinx.coroutines.flow.Flow

interface CryptoRepository {

    suspend fun getCryptoPriceListing(): Flow<Response<CryptoPriceListingDTO>>

    suspend fun getCryptoDetailsListing(): Flow<Response<CryptoDetailsListingDTO>>
}