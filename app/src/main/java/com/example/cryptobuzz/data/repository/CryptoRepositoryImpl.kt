package com.example.cryptobuzz.data.repository

import android.util.Log
import coil.network.HttpException
import com.example.cryptobuzz.data.remote.CryptoApi
import com.example.cryptobuzz.data.remote.dto.CryptoDetailsListingDTO
import com.example.cryptobuzz.data.remote.dto.CryptoPriceListingDTO
import com.example.cryptobuzz.data.utils.Response
import com.example.cryptobuzz.domain.repository.CryptoRepository
import com.google.gson.JsonParseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.SocketTimeoutException

class CryptoRepositoryImpl(
    private val cryptoApi: CryptoApi
) : CryptoRepository {
    override suspend fun getCryptoPriceListing(): Flow<Response<CryptoPriceListingDTO>> = flow {
        emit(Response.Loading())
        try {
           val result = cryptoApi.getCryptoPriceListing()
            emit(Response.Success(result))
        }
        catch (e: HttpException) {
            val errorCode = e.response.code
            val errorBody = e.response.body?.string()
            Log.e(TAG,errorBody ?: e.message ?: "Unknown Error")

            val errorMessage = when (errorCode) {
                in 400..499 -> "A client-side error occurred ($errorCode). Please check your request and try again."
                in 500..599 -> "A server-side error occurred ($errorCode). Please try again later."
                else -> "An HTTP error occurred ($errorCode). Please try again."
            }
            emit(Response.Error(message = errorMessage))
        }
        catch (e: IOException) {
            // IOException is thrown when there is a network issue like no internet
            emit(Response.Error(message = "Network error: Please check your internet connection and try again."))
        }
        catch (e: JsonParseException) {
            // Handle JSON parsing errors
            emit(Response.Error(message = "Parsing error: Data from the server couldn't be read."))
        }
        catch (e: SocketTimeoutException) {
            // Handle timeout errors specifically
            emit(Response.Error(message = "Timeout error: Server took too long to respond. Please try again later."))
        }
        catch (e: Exception) {
            Log.e(TAG, e.message ?: "Unknown Error")
            emit(Response.Error(message = "Oops, something went wrong, Please try again later."))
        }
    }

    override suspend fun getCryptoDetailsListing(): Flow<Response<CryptoDetailsListingDTO>> = flow {
        emit(Response.Loading())
        try {
            val result = cryptoApi.getCryptoDetailsListing()
            emit(Response.Success(result))
        }
        catch (e: HttpException) {
            val errorCode = e.response.code
            val errorBody = e.response.body?.string()
            Log.e(TAG,errorBody ?: e.message ?: "Unknown Error")

            val errorMessage = when (errorCode) {
                in 400..499 -> "A client-side error occurred ($errorCode). Please check your request and try again."
                in 500..599 -> "A server-side error occurred ($errorCode). Please try again later."
                else -> "An HTTP error occurred ($errorCode). Please try again."
            }
            emit(Response.Error(message = errorMessage))
        }
        catch (e: IOException) {
            // IOException is thrown when there is a network issue like no internet
            emit(Response.Error(message = "Network error: Please check your internet connection and try again."))
        }
        catch (e: JsonParseException) {
            // Handle JSON parsing errors
            emit(Response.Error(message = "Parsing error: Data from the server couldn't be read."))
        }
        catch (e: SocketTimeoutException) {
            // Handle timeout errors specifically
            emit(Response.Error(message = "Timeout error: Server took too long to respond. Please try again later."))
        }
        catch (e: Exception) {
            Log.e(TAG, e.message ?: "Unknown Error")
            emit(Response.Error(message = "Oops, something went wrong, Please try again later."))
        }
    }

    companion object{
        const val TAG ="CryptoRepositoryImpl"
    }
}