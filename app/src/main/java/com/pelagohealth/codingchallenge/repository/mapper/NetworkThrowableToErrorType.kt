package com.pelagohealth.codingchallenge.repository.mapper

import com.pelagohealth.codingchallenge.repository.model.ErrorType
import okio.IOException
import retrofit2.HttpException

fun Throwable.toErrorType() = when (this) {
    is IOException -> ErrorType.Api.Network
    is HttpException -> when (code()) {
        404 -> ErrorType.Api.NotFound
        500 -> ErrorType.Api.Server
        503 -> ErrorType.Api.ServiceUnavailable
        else -> ErrorType.Unknown
    }
    else -> ErrorType.Unknown
}