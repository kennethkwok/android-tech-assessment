package com.pelagohealth.codingchallenge.feature.fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pelagohealth.codingchallenge.repository.FactRepository
import com.pelagohealth.codingchallenge.repository.model.Fact
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
        viewModelScope.launch {
            getRandomFact()
        }
    }

    fun fetchNewFact() {
        viewModelScope.launch {
            _factUIState.value.fact?.let { factRepository.storeFactInDatabase(it) }
            getRandomFact()
        }
    }

    private suspend fun getRandomFact() {
        _factUIState.update { it.copy(loading = true) }

        factRepository.getRandomFact().collect { fact ->
            Timber.d(fact.toString())
            _factUIState.update { it.copy(loading = false, fact = fact) }
        }
    }
}

data class FactUIState (
    val loading: Boolean = false,
    val fact: Fact? = null,
)