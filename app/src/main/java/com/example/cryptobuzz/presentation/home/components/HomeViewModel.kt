package com.example.cryptobuzz.presentation.home.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptobuzz.data.remote.dto.CryptoDetailsListingDTO
import com.example.cryptobuzz.data.remote.dto.CryptoPriceListingDTO
import com.example.cryptobuzz.data.utils.Response
import com.example.cryptobuzz.domain.model.CryptoCurrency
import com.example.cryptobuzz.domain.repository.CryptoRepository
import com.example.cryptobuzz.presentation.utils.format
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cryptoRepository: CryptoRepository
) : ViewModel() {

    private var fetchCurrencyJob:Job?= null

    private val _cryptoListState = MutableStateFlow(CryptoListState())
    val cryptoListState = _cryptoListState.asStateFlow()

    private fun fetchCryptoList() {
        viewModelScope.launch {
            combine(
                cryptoRepository.getCryptoPriceListing(),
                cryptoRepository.getCryptoDetailsListing()
            ) { priceListResponse, detailsListResponse ->
                handleResponse(priceListResponse, detailsListResponse)
            }.collectLatest { cryptoList ->
                _cryptoListState.update { cryptoList }
            }
        }
    }

    private fun handleResponse(
        priceListResponse: Response<CryptoPriceListingDTO>,
        detailsListResponse: Response<CryptoDetailsListingDTO>
    ): CryptoListState {
        return when {

            priceListResponse is Response.Error -> {
                CryptoListState(
                    isLoading = false,
                    error = priceListResponse.message
                )
            }

            detailsListResponse is Response.Error -> {
                CryptoListState(
                    isLoading = false,
                    error = detailsListResponse.message
                )
            }

            priceListResponse is Response.Loading || detailsListResponse is Response.Loading -> {
                CryptoListState(isLoading = true)
            }

            priceListResponse is Response.Success && detailsListResponse is Response.Success -> {

                val result = mapResponsesToCryptoList(priceListResponse.data, detailsListResponse.data)
                CryptoListState(
                    isLoading = false,
                    error = "",
                    cryptoList = result,
                    lastUpdated = System.currentTimeMillis()
                )

            }

            else -> { CryptoListState() }
        }
    }


    private fun mapResponsesToCryptoList(
        priceData: CryptoPriceListingDTO?,
        detailsData: CryptoDetailsListingDTO?
    ): List<CryptoCurrency> {
        return priceData?.rates?.mapNotNull { (name, price) ->
            detailsData?.cryptoList?.get(name)?.let { details ->
                CryptoCurrency(
                    name = details.name,
                    fullName = details.fullName,
                    symbol = details.symbol,
                    price = price.format(6),
                    imageUrl = details.iconUrl
                )
            }
        } ?: emptyList()
    }

    fun fetchDataPeriodically() {
        fetchCurrencyJob?.cancel()
        fetchCurrencyJob = viewModelScope.launch {
            while (true) {
                fetchCryptoList()
                delay(180000L) // 3 minutes delay
            }
        }
    }

    init {
        fetchDataPeriodically()
    }


}