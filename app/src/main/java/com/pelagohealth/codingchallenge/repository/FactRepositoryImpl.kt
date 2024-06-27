package com.pelagohealth.codingchallenge.repository

import com.pelagohealth.codingchallenge.database.dao.FactDao
import com.pelagohealth.codingchallenge.database.model.FactEntity
import com.pelagohealth.codingchallenge.network.FactsApiService
import com.pelagohealth.codingchallenge.repository.mapper.toErrorType
import com.pelagohealth.codingchallenge.repository.model.Fact
import com.pelagohealth.codingchallenge.repository.model.Resource
import javax.inject.Inject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import timber.log.Timber

/**
 * Data source for providing facts
 */
class FactRepositoryImpl @Inject constructor(
    private val apiService: FactsApiService,
    private val factDao: FactDao,
) : FactRepository {

    @SuppressWarnings("TooGenericExceptionCaught")
    override fun getRandomFact() = flow {
        try {
            val response = apiService.getFact()

            if (response.isSuccessful) {
                val factDto = response.body();

                val fact = Fact(
                    id = factDto?.id.orEmpty(),
                    text = factDto?.text.orEmpty()
                )

                emit(Resource.Success(fact))
            } else {
                emit(Resource.Error(response.code().toErrorType()))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.toErrorType()))
        }
    }

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

    override fun getFactsFromDatabase(number: Int) = factDao
        .getFacts(number)
        .transform { factEntity ->
            val facts = factEntity.map { Fact(id = it.id, text = it.text.orEmpty()) }
            emit(facts)
        }

    override suspend fun removeFactFromDatabase(fact: Fact) {
        factDao.deleteFact(fact.id)
        Timber.d("Fact removed from database: $fact")
    }
}
