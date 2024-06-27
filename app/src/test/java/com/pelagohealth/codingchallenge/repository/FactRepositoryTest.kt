package com.pelagohealth.codingchallenge.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.pelagohealth.codingchallenge.TestRepository
import com.pelagohealth.codingchallenge.database.dao.FactDao
import com.pelagohealth.codingchallenge.database.model.FactEntity
import com.pelagohealth.codingchallenge.network.FactsApiService
import com.pelagohealth.codingchallenge.network.model.FactDTO
import com.pelagohealth.codingchallenge.repository.model.ErrorType
import com.pelagohealth.codingchallenge.repository.model.Fact
import com.pelagohealth.codingchallenge.repository.model.Resource
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.net.HttpURLConnection
import java.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class FactRepositoryTest {

    private lateinit var factRepository: FactRepository

    private val apiService: FactsApiService = mockk()
    private val factDao: FactDao = mockk()
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        factRepository = FactRepositoryImpl(apiService, factDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getRandomFact network request returns successfully then the random fact is emitted`() {
        runBlocking {
            val dto = FactDTO(
                id = "id",
                text = "random fact",
                source = "source",
                sourceUrl = "sourceUrl",
                language = "english",
                permalink = "permalink"
            )

            val fact = Resource.Success(Fact(id = "id", text = "random fact"))

            coEvery { apiService.getFact() } returns Response.success(dto)

            factRepository.getRandomFact().test {
                assertEquals(fact, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when getRandomFact network request returns a HTTP_NOT_FOUND error then ErrorType-Api-NotFound is emitted`() {
        runBlocking {
            val errorType = Resource.Error<Fact>(ErrorType.Api.NotFound)

            coEvery { apiService.getFact() } returns Response.error(
                HttpURLConnection.HTTP_NOT_FOUND,
                "{}".toResponseBody("application/json".toMediaTypeOrNull()),
            )

            factRepository.getRandomFact().test {
                assertEquals(errorType, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when getRandomFact network request returns a HTTP_INTERNAL_ERROR error then ErrorType-Api-Server is emitted`() {
        runBlocking {
            val errorType = Resource.Error<Fact>(ErrorType.Api.Server)

            coEvery { apiService.getFact() } returns Response.error(
                HttpURLConnection.HTTP_INTERNAL_ERROR,
                "{}".toResponseBody("application/json".toMediaTypeOrNull()),
            )

            factRepository.getRandomFact().test {
                assertEquals(errorType, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when getRandomFact network request returns a HTTP_UNAVAILABLE error then ErrorType-Api-ServiceUnavailable is emitted`() {
        runBlocking {
            val errorType = Resource.Error<Fact>(ErrorType.Api.ServiceUnavailable)

            coEvery { apiService.getFact() } returns Response.error(
                HttpURLConnection.HTTP_UNAVAILABLE,
                "{}".toResponseBody("application/json".toMediaTypeOrNull()),
            )

            factRepository.getRandomFact().test {
                assertEquals(errorType, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when getRandomFact network request returns a HTTP_BAD_METHOD error then catch all ErrorType-Unknown is emitted`() {
        runBlocking {
            val errorType = Resource.Error<Fact>(ErrorType.Unknown)

            coEvery { apiService.getFact() } returns Response.error(
                HttpURLConnection.HTTP_BAD_METHOD,
                "{}".toResponseBody("application/json".toMediaTypeOrNull()),
            )

            factRepository.getRandomFact().test {
                assertEquals(errorType, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when getRandomFact network request throws an IOException then ErrorType-Api-Network is emitted`() {
        runBlocking {
            val errorType = Resource.Error<Fact>(ErrorType.Api.Network)

            coEvery { apiService.getFact() } throws IOException("test io exception")

            factRepository.getRandomFact().test {
                assertEquals(errorType, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when getRandomFact network request throws a generic exception then catch all ErrorType-Unknown is emitted`() {
        runBlocking {
            val errorType = Resource.Error<Fact>(ErrorType.Unknown)

            coEvery { apiService.getFact() } throws InterruptedException("test interrupted exception")

            factRepository.getRandomFact().test {
                assertEquals(errorType, awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `when storeFactInDatabase is called then insertFact method is invoked`() {
        runBlocking {
            val fact = Fact(id = "id", text = "random fact")

            coJustRun { factDao.insertFact(any()) }

            factRepository.storeFactInDatabase(fact)

            coVerify(exactly = 1) { factDao.insertFact(any()) }
        }
    }

    @Test
    fun `when removeFactFromDatabase is called then deleteFact method is invoked`() {
        runBlocking {
            val fact = Fact(id = "id", text = "random fact")

            coJustRun { factDao.deleteFact(any()) }

            factRepository.removeFactFromDatabase(fact)

            coVerify(exactly = 1) { factDao.deleteFact(fact.id) }
        }
    }

    @Test
    fun `when getFactsFromDatabase is called then a list of facts are returned`() {
        runBlocking {
            val testFactEntityList = TestRepository<List<FactEntity>>()

            val fact = Fact(id = "id", text = "random fact")
            val factEntity = FactEntity(id = "id", text = "random fact", timestamp = 0L)

            every { factDao.getFacts(any()) } returns testFactEntityList.flow

            factRepository.getFactsFromDatabase(1).test() {
                // delay added to fix issues with flakiness
                delay(2000)

                testFactEntityList.emit(listOf(factEntity))

                assertEquals(listOf(fact), awaitItem())

                cancelAndConsumeRemainingEvents()
            }
        }
    }
}