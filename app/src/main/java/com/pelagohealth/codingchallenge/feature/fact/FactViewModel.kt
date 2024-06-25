package com.pelagohealth.codingchallenge.feature.fact

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FactViewModel @Inject constructor() : ViewModel() {
    fun fetchNewFact() {
    }
}