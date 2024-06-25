package com.pelagohealth.codingchallenge.repository

import com.pelagohealth.codingchallenge.network.FactsApiService
import com.pelagohealth.codingchallenge.repository.model.Fact
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Data source for providing facts
 */
class FactRepositoryImpl @Inject constructor(private val apiService: FactsApiService) : FactRepository {
    override fun getRandomFact() = flow {
        val response = apiService.getFact()

        if (response.isSuccessful) {
            val factDto = response.body();

            val fact = Fact(
                text = factDto?.text ?: "",
                url = factDto?.permalink ?: "",
            )

            emit(fact)
        } else {
            throw Exception(response.message())
        }
    }.flowOn(Dispatchers.IO)
}