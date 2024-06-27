package com.pelagohealth.codingchallenge.feature.fact

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pelagohealth.codingchallenge.R
import com.pelagohealth.codingchallenge.repository.model.ErrorType
import com.pelagohealth.codingchallenge.repository.model.Fact
import com.pelagohealth.codingchallenge.ui.theme.PelagoCodingChallengeTheme
import com.pelagohealth.codingchallenge.ui.theme.Typography

private const val CURRENT_FACT_HORIZONTAL_PADDING = 32
private const val PREVIOUS_FACT_HORIZONTAL_PADDING = 16
private const val SPACER_HEIGHT = 32
private const val ICON_SIZE = 48

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FactScreen(viewModel: FactViewModel) {
    val uiState: FactUIState by viewModel.factUIState.collectAsStateWithLifecycle()
    val previousFactsVisible = !uiState.storedFacts.isNullOrEmpty()
    val previousFacts = uiState.storedFacts ?: listOf()

    PelagoCodingChallengeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(
                                vertical = 24.dp,
                                horizontal = CURRENT_FACT_HORIZONTAL_PADDING.dp
                            ),
                    ) {
                        CurrentFact(
                            isLoading = uiState.loading,
                            errorType = uiState.currentFactErrorType,
                            fact = uiState.fact?.text ?: ""
                        ) {
                            viewModel.fetchNewFact()
                        }
                    }
                }

                item {
                    AnimatedVisibility(previousFactsVisible) {
                        Text(
                            stringResource(id = R.string.title_previous_viewed_facts),
                            modifier = Modifier
                                .padding(
                                    horizontal = PREVIOUS_FACT_HORIZONTAL_PADDING.dp,
                                    vertical = 8.dp
                                ),
                            style = Typography.titleLarge
                        )
                    }
                }

                items(
                    previousFacts,
                    key = { fact -> fact.id },
                ) { fact ->
                    PreviousFactItem(
                        modifier = Modifier.animateItemPlacement(),
                        fact = fact,
                    ) {
                        viewModel.removeFact(fact)
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentFact(
    isLoading: Boolean,
    errorType: ErrorType?,
    fact: String,
    onClickMoreFacts: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            contentAlignment = Alignment.Center
        ) {

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(ICON_SIZE.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            } else if (errorType != null) {
                val errorMessage = determineErrorMessage(errorType = errorType)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Warning,
                        modifier = Modifier.padding(vertical = 8.dp).size(ICON_SIZE.dp),
                        contentDescription = errorMessage
                    )
                    Text(
                        text = errorMessage,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Text(
                    text = fact,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height((SPACER_HEIGHT / 2).dp))

        Button(
            enabled = !isLoading,
            onClick = onClickMoreFacts,
        ) {
            Text(stringResource(id = R.string.button_more_facts))
        }

        Spacer(modifier = Modifier.height(SPACER_HEIGHT.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreviousFactItem(
    modifier: Modifier,
    fact: Fact,
    onDismiss: () -> Unit
) {
    val dismissBoxState = rememberSwipeToDismissBoxState(
        positionalThreshold = { it * 0.2f },
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> onDismiss()
                SwipeToDismissBoxValue.EndToStart -> onDismiss()
                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        }
    )

    SwipeToDismissBox(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = PREVIOUS_FACT_HORIZONTAL_PADDING.dp),
        state = dismissBoxState,
        backgroundContent = {
            Row(
                modifier = Modifier.fillMaxSize(),
                content = {},
            )
        },
    ) {
        ListItem(
            shadowElevation = 4.dp,
            headlineContent = {
                Text(fact.text)
            },
        )
    }
}

@Composable
private fun determineErrorMessage(errorType: ErrorType): String {
    return when (errorType) {
        is ErrorType.Api.Network -> stringResource(id = R.string.error_api_network)
        is ErrorType.Api.NotFound -> stringResource(id = R.string.error_api_not_found)
        is ErrorType.Api.Server -> stringResource(id = R.string.error_api_server)
        is ErrorType.Api.ServiceUnavailable -> stringResource(id = R.string.error_api_service)
        else -> stringResource(id = R.string.error_unknown) 
    }
}