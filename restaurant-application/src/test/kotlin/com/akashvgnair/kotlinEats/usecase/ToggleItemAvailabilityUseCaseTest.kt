package com.akashvgnair.kotlinEats.usecase

import com.akashvgnair.kotlinEats.dto.ToggleItemAvailabilityCommand
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ToggleItemAvailabilityUseCaseTest {

    private val restaurantId = RestaurantId()
    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var menuRepository: MenuRepository
    private lateinit var eventBus: EventBus
    private lateinit var toggleUseCase: ToggleItemAvailabilityUseCase

    private fun createCommand(
        restaurantId: String = this.restaurantId.value.toString(),
        menuItemId: String = this.menuItem.id.value.toString()
    ): ToggleItemAvailabilityCommand {
        return ToggleItemAvailabilityCommand(restaurantId, menuItemId)
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

        toggleUseCase = ToggleItemAvailabilityUseCase(menuRepository, eventBus)
    }

    @Test
    fun `toggles item from available to unavailable`() {
        assertTrue(menuItem.available)

        val result = toggleUseCase.execute(createCommand())

        assertTrue(result is Result.Success)
        assertFalse(result.getOrNull()!!.available)
    }

    @Test
    fun `toggles item back to available`() {
        toggleUseCase.execute(createCommand())
        val result = toggleUseCase.execute(createCommand())

        assertTrue(result is Result.Success)
        assertTrue(result.getOrNull()!!.available)
    }

    @Test
    fun `persists the toggled state`() {
        toggleUseCase.execute(createCommand())

        val menuFromDb = menuRepository.findByRestaurantId(restaurantId)!!
        val itemFromDb = menuFromDb.getMenuItemByMenuItemId(menuItem.id)!!
        assertFalse(itemFromDb.available)
    }

    @Test
    fun `fails when menu not found`() {
        val command = createCommand(restaurantId = RestaurantId().value.toString())
        val result = toggleUseCase.execute(command)

        assertTrue(result is Result.Failure)
        assertEquals("Menu not found for the restaurant", (result as Result.Failure).error)
    }

    @Test
    fun `fails when menu item not found`() {
        val command = createCommand(menuItemId = MenuItemId().value.toString())
        val result = toggleUseCase.execute(command)

        assertTrue(result is Result.Failure)
        assertEquals("Menu item not found in the menu", (result as Result.Failure).error)
    }
}