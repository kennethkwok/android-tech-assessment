package com.pelagohealth.codingchallenge.repository

import com.pelagohealth.codingchallenge.repository.model.Fact
import com.pelagohealth.codingchallenge.repository.model.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for providing random facts.
 */
interface FactRepository {
    fun getRandomFact(): Flow<Resource<Fact>>

    suspend fun storeFactInDatabase(fact: Fact)

    fun getFactsFromDatabase(number: Int): Flow<List<Fact>>

    suspend fun removeFactFromDatabase(fact: Fact)
}