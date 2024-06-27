package com.pelagohealth.codingchallenge.network


import com.pelagohealth.codingchallenge.network.model.FactDTO
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.net.HttpURLConnection
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FactsApiServiceTest {

    private lateinit var server: MockWebServer
    private lateinit var api: FactsApiService

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Before
    fun setup() {
        server = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build().create(FactsApiService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `when getFact is requested then a FactDTO object is returned`() {
        runTest {
            val dto = FactDTO(
                id = "id",
                text = "random fact",
                source = "source",
                sourceUrl = "sourceUrl",
                language = "english",
                permalink = "permalink"
            )

            val json = moshi.adapter(FactDTO::class.java).toJson(dto)

            val response = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(json)
            server.enqueue(response)

            val fact = api.getFact()
            val request = server.takeRequest()

            assertEquals("/facts/random", request.path)
            assertEquals(true, fact.isSuccessful)
            assertEquals(dto, fact.body())
        }
    }

    @Test
    fun `when getFact is requested and network error occurs then error object is returned`() {
        runTest {
            val errorBody = "{ \"error\": \"error\" }"
            val res = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                .setBody(errorBody)
            server.enqueue(res)

            val data = api.getFact()
            server.takeRequest()

            assertEquals(false, data.isSuccessful)
            assertEquals(errorBody, data.errorBody()?.string())
        }
    }
}