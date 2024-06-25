package com.pelagohealth.codingchallenge.feature.fact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pelagohealth.codingchallenge.R
import com.pelagohealth.codingchallenge.ui.theme.PelagoCodingChallengeTheme

@Composable
fun FactScreen(viewModel: FactViewModel) {
    val uiState: FactUIState by viewModel.factUIState.collectAsStateWithLifecycle()

    PelagoCodingChallengeTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 16.dp),
                        text = uiState.fact ?: "",
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    enabled = !uiState.loading,
                    onClick = { viewModel.fetchNewFact() },
                ) {
                    Text(stringResource(id = R.string.button_more_facts))
                }
            }
        }
    }
}