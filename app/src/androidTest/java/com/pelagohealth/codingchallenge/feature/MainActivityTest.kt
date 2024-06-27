package com.pelagohealth.codingchallenge.feature

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.pelagohealth.codingchallenge.MockServerDispatcher
import com.pelagohealth.codingchallenge.feature.fact.BUTTON_MORE_FACTS
import com.pelagohealth.codingchallenge.feature.fact.CURRENT_FACT
import com.pelagohealth.codingchallenge.feature.fact.LIST_PREVIOUS_FACTS_HEADING
import com.pelagohealth.codingchallenge.feature.fact.LIST_PREVIOUS_FACTS_ITEM
import com.pelagohealth.codingchallenge.feature.fact.LIST_PREVIOUS_FACTS_ITEM_INNER
import com.pelagohealth.codingchallenge.feature.fact.LOADING_SPINNER
import com.pelagohealth.codingchallenge.navigation.AppNavGraph
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val UI_IDLE_TIMEOUT = 5000L

@HiltAndroidTest
class MainActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: TestNavHostController

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        hiltRule.inject()

        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)

        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            AppNavGraph(navController = navController)
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun When_MainActivityLaunches_Then_FactScreenIsDisplayed_And_PreviousFactsAreNotVisible() {
        composeTestRule.apply {
            onNodeWithTag(CURRENT_FACT).assertIsNotDisplayed()
            onNodeWithTag(LOADING_SPINNER).assertIsDisplayed()
            onNodeWithTag(BUTTON_MORE_FACTS).assertIsDisplayed().assertIsNotEnabled()

            waitUntil(timeoutMillis = UI_IDLE_TIMEOUT) {
                onNodeWithTag(CURRENT_FACT).isDisplayed()
            }
            onNodeWithTag(LOADING_SPINNER).assertIsNotDisplayed()
            onNodeWithTag(BUTTON_MORE_FACTS).assertIsEnabled()

            onNodeWithTag(LIST_PREVIOUS_FACTS_HEADING).assertIsNotDisplayed()
        }
    }

    @Test
    fun When_MoreFactsButtonClicked_Then_NewFactDisplayed_And_PreviousFactsAreVisible() {
        composeTestRule.apply {
            waitUntil(timeoutMillis = UI_IDLE_TIMEOUT) {
                onNodeWithTag(CURRENT_FACT).isDisplayed()
            }

            onNodeWithTag(BUTTON_MORE_FACTS).assertIsDisplayed().performClick()

            onNodeWithTag(CURRENT_FACT).assertIsNotDisplayed()
            onNodeWithTag(BUTTON_MORE_FACTS).assertIsNotEnabled()
            onNodeWithTag(LOADING_SPINNER).assertIsDisplayed()

            waitUntil(timeoutMillis = UI_IDLE_TIMEOUT) {
                onNodeWithTag(CURRENT_FACT).isDisplayed()
            }

            onNodeWithTag(LIST_PREVIOUS_FACTS_HEADING).assertIsDisplayed()
        }
    }

    @Test
    fun When_PreviousFactListItemSwipedLeft_Then_PreviousFactItemIsDeleted_And_PreviousFactsAreNotVisible() {
        composeTestRule.apply {
            waitUntil(timeoutMillis = UI_IDLE_TIMEOUT) {
                onNodeWithTag(CURRENT_FACT).isDisplayed()
            }

            onNodeWithTag(BUTTON_MORE_FACTS).assertIsDisplayed().performClick()

            waitUntil(timeoutMillis = UI_IDLE_TIMEOUT) {
                onNodeWithTag(CURRENT_FACT).isDisplayed()
            }

            onNodeWithTag(LIST_PREVIOUS_FACTS_HEADING).assertIsDisplayed()
            onNodeWithTag(LIST_PREVIOUS_FACTS_ITEM.plus(" 1")).assertIsDisplayed()
            onNodeWithTag(LIST_PREVIOUS_FACTS_ITEM_INNER.plus(" 1")).performTouchInput { swipeLeft() }

            waitUntil(timeoutMillis = UI_IDLE_TIMEOUT) {
                onNodeWithTag(LIST_PREVIOUS_FACTS_ITEM.plus(" 1")).isNotDisplayed()
            }

            onNodeWithTag(LIST_PREVIOUS_FACTS_HEADING).assertIsNotDisplayed()
        }
    }

    @Test
    fun When_PreviousFactListItemSwipedRight_Then_PreviousFactItemIsDeleted_And_PreviousFactsAreNotVisible() {
        composeTestRule.apply {
            waitUntil(timeoutMillis = UI_IDLE_TIMEOUT) {
                onNodeWithTag(CURRENT_FACT).isDisplayed()
            }

            onNodeWithTag(BUTTON_MORE_FACTS).assertIsDisplayed().performClick()

            waitUntil(timeoutMillis = UI_IDLE_TIMEOUT) {
                onNodeWithTag(CURRENT_FACT).isDisplayed()
            }

            onNodeWithTag(LIST_PREVIOUS_FACTS_HEADING).assertIsDisplayed()
            onNodeWithTag(LIST_PREVIOUS_FACTS_ITEM.plus(" 1")).assertIsDisplayed()
            onNodeWithTag(LIST_PREVIOUS_FACTS_ITEM_INNER.plus(" 1")).performTouchInput { swipeRight() }

            waitUntil(timeoutMillis = UI_IDLE_TIMEOUT) {
                onNodeWithTag(LIST_PREVIOUS_FACTS_ITEM.plus(" 1")).isNotDisplayed()
            }

            onNodeWithTag(LIST_PREVIOUS_FACTS_HEADING).assertIsNotDisplayed()
        }
    }
}