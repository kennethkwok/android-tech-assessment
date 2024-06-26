package com.pelagohealth.codingchallenge.feature.fact

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

private const val HORIZONTAL_PADDING = 32

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FactScreen(viewModel: FactViewModel) {
    val uiState: FactUIState by viewModel.factUIState.collectAsStateWithLifecycle()

    PelagoCodingChallengeTheme {
        Scaffold { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 24.dp, horizontal = HORIZONTAL_PADDING.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CurrentFact(
                            isLoading = uiState.loading,
                            fact = uiState.fact?.text ?: ""
                        ) {
                            viewModel.fetchNewFact()
                        }
                    }
                }

                val previousFacts = uiState.storedFacts
                if (!previousFacts.isNullOrEmpty()) {
                    items(
                        previousFacts,
                        key = { fact -> fact.id },
                    ) { fact ->
                        ListItem(
                            modifier = Modifier.animateItemPlacement(),
                            headlineContent = {
                                Text(fact.text)
                            },
                        )
                        HorizontalDivider(modifier = Modifier.animateItemPlacement())
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentFact(
    isLoading: Boolean,
    fact: String,
    onClickMoreFacts: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        } else {
            Text(
                text = fact,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            enabled = !isLoading,
            onClick = onClickMoreFacts,
        ) {
            Text(stringResource(id = R.string.button_more_facts))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}