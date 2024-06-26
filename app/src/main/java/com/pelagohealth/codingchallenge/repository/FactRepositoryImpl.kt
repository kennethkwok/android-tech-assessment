package com.pelagohealth.codingchallenge.repository

import com.pelagohealth.codingchallenge.database.dao.FactDao
import com.pelagohealth.codingchallenge.database.model.FactEntity
import com.pelagohealth.codingchallenge.network.FactsApiService
import com.pelagohealth.codingchallenge.repository.model.Fact
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

/**
 * Data source for providing facts
 */
class FactRepositoryImpl @Inject constructor(
    private val apiService: FactsApiService,
    private val factDao: FactDao,
) : FactRepository {
    override fun getRandomFact() = flow {
        val response = apiService.getFact()

        if (response.isSuccessful) {
            val factDto = response.body();

            val fact = Fact(
                id = factDto?.id ?: "",
                text = factDto?.text ?: ""
            )

            emit(fact)
        } else {
            throw Exception(response.message())
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun storeFactInDatabase(fact: Fact) {
        val timestamp = System.currentTimeMillis()

        val factEntity = FactEntity(
            id = fact.id,
            text = fact.text,
            timestamp = timestamp
        )

        factDao.insertFact(factEntity)
        Timber.d("Fact stored in database: $fact")
    }

    override fun getFactsFromDatabase(number: Int) = flow {
        val facts = factDao
            .getFacts(number)
            .map { Fact(id = it.id, text = it.text ?: "") }

        emit(facts)
    }.flowOn(Dispatchers.IO)

    override suspend fun removeFactFromDatabase(fact: Fact) {
        factDao.deleteFact(fact.id)
        Timber.d("Fact removed from database: $fact")
    }
}