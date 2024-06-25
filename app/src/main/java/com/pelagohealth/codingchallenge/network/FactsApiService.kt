package com.pelagohealth.codingchallenge.network

import com.pelagohealth.codingchallenge.network.model.FactDTO
import retrofit2.Response
import retrofit2.http.GET

/**
 * REST API for fetching random facts.
 */
interface FactsApiService {
    @GET("facts/random")
    suspend fun getFact(): Response<FactDTO>
}