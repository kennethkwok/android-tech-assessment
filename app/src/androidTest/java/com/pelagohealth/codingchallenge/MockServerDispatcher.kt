package com.pelagohealth.codingchallenge

import com.pelagohealth.codingchallenge.util.FileReader
import java.util.concurrent.TimeUnit
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class MockServerDispatcher {
    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "/facts/random" -> MockResponse()
                    .setResponseCode(200)
                    .setHeadersDelay(1, TimeUnit.SECONDS)
                    .setBody(FileReader.readStringFromFile("random_fact.json"))
                else -> MockResponse().setResponseCode(404)
            }
        }
    }

    internal inner class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(500)
        }
    }
}
