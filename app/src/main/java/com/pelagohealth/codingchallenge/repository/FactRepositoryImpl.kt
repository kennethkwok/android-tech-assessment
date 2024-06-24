package com.pelagohealth.codingchallenge.repository

import com.pelagohealth.codingchallenge.network.FactsApiService
import com.pelagohealth.codingchallenge.repository.model.Fact
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Data source for providing facts
 */
class FactRepositoryImpl @Inject constructor(private val apiService: FactsApiService) : FactRepository {
    override fun getRandomFact(): Flow<Fact> {
        TODO("Not yet implemented")
    }
}