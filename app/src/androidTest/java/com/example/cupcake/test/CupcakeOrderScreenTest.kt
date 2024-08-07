package com.example.cupcake.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.cupcake.ui.SelectOptionScreen
import com.example.cupcake.R
import com.example.cupcake.data.DataSource
import com.example.cupcake.data.OrderUiState
import com.example.cupcake.ui.OrderSummaryScreen
import com.example.cupcake.ui.StartOrderScreen
import org.junit.Rule
import org.junit.Test

class CupcakeOrderScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun selectOptionScreen_verifyContent() {
        // Given list of options
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // And subtotal
        val subtotal = "$100"

        // When SelectOptionScreen is loaded
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = subtotal, options = flavors)
        }

        // Then all the options are displayed on the screen.
        flavors.forEach { flavor ->
            composeTestRule.onNodeWithText(flavor).assertIsDisplayed()
        }

        // And then the subtotal is displayed correctly.
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                subtotal
            )
        ).assertIsDisplayed()

        // And then the next button is disabled
        composeTestRule.onNodeWithStringId(R.string.next).assertIsNotEnabled()
    }

    @Test
    fun startScreen_verifyContent() {
        composeTestRule.setContent {
            StartOrderScreen(DataSource.quantityOptions, {})
        }

        DataSource.quantityOptions.forEach {
            option -> composeTestRule.onNodeWithStringId(option.first).assertIsDisplayed()
        }
    }

    @Test
    fun summaryScreen_verifyContent() {
        val state = OrderUiState(quantity = 6, flavor = "Vanilla", date = "Wed Aug 7", price = "£24")

        composeTestRule.setContent {
            OrderSummaryScreen(orderUiState = state, {}, { _, _ -> })
        }

        composeTestRule.onNodeWithText(state.flavor).assertIsDisplayed()
        composeTestRule.onNodeWithText(state.date).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.subtotal_price, state.price)
        ).assertIsDisplayed()

    }

    @Test
    fun flavourScreen_verifyNextButtonEnabledWhenOptionSelected() {
        composeTestRule.setContent {
            SelectOptionScreen(subtotal = "£100", options = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango"))
        }

        composeTestRule.onNodeWithText("Vanilla").performClick()

        composeTestRule.onNodeWithStringId(R.string.next).assertIsEnabled()
    }
}