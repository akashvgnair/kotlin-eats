package com.akashvgnair.kotlinEats.usecase

import com.akashvgnair.kotlinEats.dto.UpdateMenuItemPriceCommand
import com.akashvgnair.kotlinEats.events.EventBus
import com.akashvgnair.kotlinEats.models.Menu
import com.akashvgnair.kotlinEats.models.MenuItem
import com.akashvgnair.kotlinEats.models.MenuItemId
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.ports.MenuRepository
import com.akashvgnair.kotlinEats.types.Money
import com.akashvgnair.kotlinEats.types.Result
import com.akashvgnair.kotlinEats.usecase.fixtures.MockEventBus
import com.akashvgnair.kotlinEats.usecase.fixtures.MockMenuRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateMenuItemPriceUseCaseTest {

    private val restaurantId = RestaurantId()
    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var menuRepository: MenuRepository
    private lateinit var eventBus: EventBus
    private lateinit var updateMenuItemPriceUseCase: UpdateMenuItemPriceUseCase

    private fun createCommand(
        restaurantId: String = this.restaurantId.value.toString(),
        menuItemId: String = this.menuItem.id.value.toString(),
        price: Double = 20.0,
        currency: String = "EUR"
    ): UpdateMenuItemPriceCommand {
        return UpdateMenuItemPriceCommand(restaurantId, menuItemId, price, currency)
    }

    @BeforeEach
    fun setup() {
        menuRepository = MockMenuRepository()
        eventBus = MockEventBus()
        menu = Menu.from(restaurantId)
        menuItem = MenuItem.from("Margherita", "Pizza", Money.from(12.99))
        menu.addMenuItem(menuItem)
        menu.clearEvents()
        menuRepository.save(menu)

        updateMenuItemPriceUseCase = UpdateMenuItemPriceUseCase(menuRepository, eventBus)
    }

    @Test
    fun `updates price successfully`() {
        val command = createCommand(price = 15.50)
        val result = updateMenuItemPriceUseCase.execute(command)

        assertTrue(result is Result.Success)
        assertEquals(Money.from(15.50), result.getOrNull()?.price)

        val menuFromDb = menuRepository.findByRestaurantId(restaurantId)!!
        val itemFromDb = menuFromDb.getMenuItemByMenuItemId(menuItem.id)!!
        assertEquals(Money.from(15.50), itemFromDb.price)
    }

    @Test
    fun `fails when menu not found`() {
        val command = createCommand(restaurantId = RestaurantId().value.toString())
        val result = updateMenuItemPriceUseCase.execute(command)

        assertTrue(result is Result.Failure)
        assertEquals("Menu not found for the restaurant", (result as Result.Failure).error)
    }

    @Test
    fun `fails when menu item not found`() {
        val command = createCommand(menuItemId = MenuItemId().value.toString())
        val result = updateMenuItemPriceUseCase.execute(command)

        assertTrue(result is Result.Failure)
        assertEquals("Menu item not found in the menu", (result as Result.Failure).error)
    }

    @Test
    fun `fails when price is zero`() {
        val command = createCommand(price = 0.0)
        val result = updateMenuItemPriceUseCase.execute(command)

        assertTrue(result is Result.Failure)
    }

    @Test
    fun `fails when price is negative`() {
        val command = createCommand(price = -5.0)
        val result = updateMenuItemPriceUseCase.execute(command)

        assertTrue(result is Result.Failure)
    }

    @Test
    fun `fails when currency mismatch`() {
        val command = createCommand(currency = "USD")
        val result = updateMenuItemPriceUseCase.execute(command)

        assertTrue(result is Result.Failure)
        assertEquals("Currency should be same", (result as Result.Failure).error)
    }
}