package com.pelagohealth.codingchallenge.feature.fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pelagohealth.codingchallenge.repository.FactRepository
import com.pelagohealth.codingchallenge.repository.model.ErrorType
import com.pelagohealth.codingchallenge.repository.model.Fact
import com.pelagohealth.codingchallenge.repository.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class FactViewModel @Inject constructor(private val factRepository: FactRepository) : ViewModel() {

    private val _factUIState = MutableStateFlow(FactUIState())
    val factUIState = _factUIState.asStateFlow()

    init {
        // Retrieve facts in parallel
        viewModelScope.launch { getRandomFact() }
        viewModelScope.launch { getFactsFromDatabase() }
    }

    fun fetchNewFact() {
        viewModelScope.launch {
            _factUIState.value.fact?.let { factRepository.storeFactInDatabase(it) }
            getRandomFact()
            getFactsFromDatabase()
        }
    }

    fun removeFact(fact: Fact) {
        viewModelScope.launch {
            factRepository.removeFactFromDatabase(fact)
            getFactsFromDatabase()
        }
    }

    private suspend fun getRandomFact() {
        _factUIState.update { it.copy(loading = true, currentFactErrorType = null) }

        factRepository.getRandomFact().collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    Timber.d(resource.data.toString())
                    _factUIState.update { it.copy(loading = false, fact = resource.data) }
                }
                is Resource.Error -> {
                    Timber.d(resource.error.toString())
                    _factUIState.update { it.copy(loading = false, currentFactErrorType = resource.error) }
                }
            }
        }
    }

    private suspend fun getFactsFromDatabase() {
        factRepository.getFactsFromDatabase(3).collect { facts ->
            Timber.d(facts.toString())
            _factUIState.update { it.copy(storedFacts = facts) }

        }
    }
}

data class FactUIState (
    val loading: Boolean = false,
    val fact: Fact? = null,
    val storedFacts: List<Fact>? = null,
    val currentFactErrorType: ErrorType? = null,
)