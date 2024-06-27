package com.pelagohealth.codingchallenge.repository.mapper

import com.pelagohealth.codingchallenge.repository.model.ErrorType
import java.net.HttpURLConnection
import okio.IOException
import retrofit2.HttpException

fun Throwable.toErrorType() = when (this) {
    is IOException -> ErrorType.Api.Network
    is HttpException -> code().toErrorType()
    else -> ErrorType.Unknown
}

fun Int.toErrorType() = when (this) {
    HttpURLConnection.HTTP_NOT_FOUND -> ErrorType.Api.NotFound
    HttpURLConnection.HTTP_INTERNAL_ERROR -> ErrorType.Api.Server
    HttpURLConnection.HTTP_UNAVAILABLE -> ErrorType.Api.ServiceUnavailable
    else -> ErrorType.Unknown
}
