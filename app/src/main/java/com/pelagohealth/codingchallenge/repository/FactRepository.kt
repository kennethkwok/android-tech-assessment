package com.pelagohealth.codingchallenge.repository

import com.pelagohealth.codingchallenge.repository.model.Fact
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for providing random facts.
 */
interface FactRepository {
    fun getRandomFact(): Flow<Fact>

    suspend fun storeFactInDatabase(fact: Fact)
}