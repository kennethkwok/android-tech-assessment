package com.pelagohealth.codingchallenge.feature.fact

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.pelagohealth.codingchallenge.TestRepository
import com.pelagohealth.codingchallenge.repository.FactRepositoryImpl
import com.pelagohealth.codingchallenge.repository.model.ErrorType
import com.pelagohealth.codingchallenge.repository.model.Fact
import com.pelagohealth.codingchallenge.repository.model.Resource
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class FactViewModelTest {
    private lateinit var viewModel: FactViewModel

    private val repo: FactRepositoryImpl = mockk()
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when view model is initialised then random fact and previously viewed facts are returned`() {
        runBlocking {
            val testRandomFact = TestRepository<Resource<Fact>>()
            val testPreviousFacts = TestRepository<List<Fact>>()

            val mockFact = Fact("id", "random fact")
            val mockResource = Resource.Success(mockFact)
            val mockPreviousFacts = listOf(Fact("id2", "random fact 2"))

            every { repo.getRandomFact() } returns testRandomFact.flow
            every { repo.getFactsFromDatabase(any()) } returns testPreviousFacts.flow

            viewModel = FactViewModel(repo)

            viewModel.factUIState.test {
                assertEquals(FactUIState(loading = true), awaitItem())

                testRandomFact.emit(mockResource)

                assertEquals(
                    FactUIState(loading = false, fact = mockFact),
                    awaitItem()
                )

                testPreviousFacts.emit(mockPreviousFacts)

                assertEquals(
                    FactUIState(loading = false, fact = mockFact, storedFacts = mockPreviousFacts),
                    awaitItem()
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when error occurs fetching random fact from the API then an error type is returned`() {
        runBlocking {
            val testRandomFact = TestRepository<Resource<Fact>>()
            val testPreviousFacts = TestRepository<List<Fact>>()

            val mockErrorType = ErrorType.Api.Network
            val mockResource = Resource.Error<Fact>(mockErrorType)

            every { repo.getRandomFact() } returns testRandomFact.flow
            every { repo.getFactsFromDatabase(any()) } returns testPreviousFacts.flow

            viewModel = FactViewModel(repo)

            viewModel.factUIState.test {
                assertEquals(FactUIState(loading = true), awaitItem())

                testRandomFact.emit(mockResource)

                assertEquals(
                    FactUIState(loading = false, currentFactErrorType = mockErrorType),
                    awaitItem()
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when fetch new fact is called then currently displayed fact is stored and new random fact is returned`() {
        runBlocking {
            val testRepository = TestRepository<Resource<Fact>>()
            val testPreviousFacts = TestRepository<List<Fact>>()

            val mockFact = Fact("id", "random fact")
            val mockResource = Resource.Success(mockFact)
            val mockPreviousFacts = listOf(Fact("id2", "random fact 2"))

            every { repo.getRandomFact() } returns testRepository.flow
            coJustRun { repo.storeFactInDatabase(any()) }
            every { repo.getFactsFromDatabase(any()) } returns testPreviousFacts.flow

            viewModel = FactViewModel(repo)

            viewModel.factUIState.test {
                assertEquals(FactUIState(loading = true), awaitItem())

                testRepository.emit(mockResource)

                assertEquals(
                    FactUIState(loading = false, fact = mockFact),
                    awaitItem()
                )

                viewModel.fetchNewFact()
                assertEquals(
                    FactUIState(loading = true, fact = mockFact),
                    awaitItem()
                )

                testRepository.emit(mockResource)

                assertEquals(
                    FactUIState(loading = false, fact = mockFact),
                    awaitItem()
                )

                testPreviousFacts.emit(mockPreviousFacts)

                assertEquals(
                    FactUIState(loading = false, fact = mockFact, storedFacts = mockPreviousFacts),
                    awaitItem()
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when remove fact is called then updated previously viewed facts are returned`() {
        runBlocking {
            val testRepository = TestRepository<Resource<Fact>>()
            val testPreviousFacts = TestRepository<List<Fact>>()

            val mockFact = Fact("id", "random fact")
            val mockResource = Resource.Success(mockFact)
            val mockPreviousFacts = listOf(Fact("id2", "random fact 2"))

            every { repo.getRandomFact() } returns testRepository.flow
            coJustRun { repo.removeFactFromDatabase(any()) }
            every { repo.getFactsFromDatabase(any()) } returns testPreviousFacts.flow

            viewModel = FactViewModel(repo)

            viewModel.factUIState.test {
                assertEquals(FactUIState(loading = true), awaitItem())

                testRepository.emit(mockResource)

                assertEquals(
                    FactUIState(loading = false, fact = mockFact),
                    awaitItem()
                )

                viewModel.removeFact(mockFact)
                testPreviousFacts.emit(mockPreviousFacts)

                assertEquals(
                    FactUIState(loading = false, fact = mockFact, storedFacts = mockPreviousFacts),
                    awaitItem()
                )

                cancelAndConsumeRemainingEvents()
            }
        }
    }
}