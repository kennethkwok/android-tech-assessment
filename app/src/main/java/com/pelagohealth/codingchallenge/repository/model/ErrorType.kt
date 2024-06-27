package com.pelagohealth.codingchallenge.repository.model

/**
 * Error types which can be returned if fact request fails
 */
sealed class ErrorType {
    sealed class Api: ErrorType() {
        data object Network: Api()
        data object ServiceUnavailable : Api()
        data object NotFound : Api()
        data object Server : Api()
    }

    data object Unknown: ErrorType()
}

