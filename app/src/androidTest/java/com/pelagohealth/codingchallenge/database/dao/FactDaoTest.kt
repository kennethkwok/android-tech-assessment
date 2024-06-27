package com.pelagohealth.codingchallenge.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.pelagohealth.codingchallenge.database.FactDatabase
import com.pelagohealth.codingchallenge.database.model.FactEntity
import java.util.concurrent.CountDownLatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class FactDaoTest {
    private lateinit var database: FactDatabase
    private lateinit var factDao: FactDao

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FactDatabase::class.java
        ).allowMainThreadQueries().build()

        factDao = database.factDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun When_FactInserted_Then_FactIsVisibleInDatabase() {
        runBlocking {
            val fact = FactEntity(id = "id", text = "random fact", timestamp = 1L)
            factDao.insertFact(fact)

            val latch = CountDownLatch(1)
            val job = async(Dispatchers.IO) {
                factDao.getFacts(1).collect {
                    assertEquals(1, it.size)
                    assertEquals("id", it.first().id)
                    assertEquals("random fact", it.first().text)
                    latch.countDown()
                }
            }

            latch.await()
            job.cancelAndJoin()
        }
    }

    @Test
    fun When_FactDeleted_Then_FactIsNoLongerVisibleInDatabase() {
        runBlocking {
            val fact = FactEntity(id = "id", text = "random fact", timestamp = 1L)
            val fact2 = FactEntity(id = "id 2", text = "random fact 2", timestamp = 1L)

            factDao.insertFact(fact)
            factDao.insertFact(fact2)
            factDao.deleteFact(fact.id)

            val latch = CountDownLatch(1)
            val job = async(Dispatchers.IO) {
                factDao.getFacts(3).collect {
                    assertEquals(1, it.size)
                    assertEquals("id 2", it.first().id)
                    assertEquals("random fact 2", it.first().text)
                    latch.countDown()
                }
            }

            latch.await()
            job.cancelAndJoin()
        }
    }

    @Test
    fun When_getFactsIsInvoked_Then_FactsAreReturnedInOrderOfDescendingTimestamp() {
        runBlocking {
            val fact = FactEntity(id = "id", text = "random fact", timestamp = 1L)
            val fact2 = FactEntity(id = "id 2", text = "random fact 2", timestamp = 2L)

            factDao.insertFact(fact)
            factDao.insertFact(fact2)

            val latch = CountDownLatch(1)
            val job = async(Dispatchers.IO) {
                factDao.getFacts(3).collect {
                    assertEquals(2, it.size)
                    assertEquals("id 2", it.first().id)
                    assertEquals("random fact 2", it.first().text)
                    assertEquals("id", it[1].id)
                    assertEquals("random fact", it[1].text)
                    latch.countDown()
                }
            }

            latch.await()
            job.cancelAndJoin()
        }
    }
}