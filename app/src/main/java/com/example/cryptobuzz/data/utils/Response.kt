package com.example.cryptobuzz.data.utils

sealed class Response<T>(
    val data:T? = null,
    val message:String = ""
) {
    class Loading<T>:Response<T>()
    class Success<T>(data: T):Response<T>(data = data)
    class Error<T>(message: String):Response<T>(message = message)
}