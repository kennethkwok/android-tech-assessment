package com.pelagohealth.codingchallenge

import kotlinx.coroutines.flow.MutableSharedFlow

class TestRepository<T> {
    val flow = MutableSharedFlow<T>()

    suspend fun emit(value: T) {
        flow.emit(value)
    }
}