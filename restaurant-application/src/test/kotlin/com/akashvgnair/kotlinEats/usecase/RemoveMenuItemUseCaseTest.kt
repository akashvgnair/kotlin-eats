package com.akashvgnair.kotlinEats.usecase

import com.akashvgnair.kotlinEats.dto.RemoveMenuItemCommand
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

class RemoveMenuItemUseCaseTest {

    private val restaurantId = RestaurantId()
    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var menuRepository: MenuRepository
    private lateinit var eventBus: EventBus
    private lateinit var removeMenuItemUseCase: RemoveMenuItemUseCase

    @BeforeEach
    fun setup() {
        menuRepository = MockMenuRepository()
        eventBus = MockEventBus()
        menu = Menu.from(restaurantId)
        menuItem = MenuItem.from("Margherita", "Pizza", Money.from(12.99))
        menu.addMenuItem(menuItem)
        menu.clearEvents()
        menuRepository.save(menu)

        removeMenuItemUseCase = RemoveMenuItemUseCase(menuRepository, eventBus)
    }

    @Test
    fun `removes menu item successfully`() {
        val command = RemoveMenuItemCommand(
            restaurantId = restaurantId.value.toString(),
            menuItemId = menuItem.id.value.toString()
        )

        val result = removeMenuItemUseCase.execute(command)

        assertTrue(result is Result.Success)
        assertEquals(menuItem.id, result.getOrNull()?.id)

        val menuFromDb = menuRepository.findByRestaurantId(restaurantId)!!
        assertTrue(menuFromDb.getAllMenuItems().isEmpty())
    }

    @Test
    fun `fails when restaurant has no menu`() {
        val command = RemoveMenuItemCommand(
            restaurantId = RestaurantId().value.toString(),
            menuItemId = menuItem.id.value.toString()
        )

        val result = removeMenuItemUseCase.execute(command)

        assertTrue(result is Result.Failure)
        assertEquals("Menu not found for the restaurant", (result as Result.Failure).error)
    }

    @Test
    fun `fails when menu item does not exist`() {
        val command = RemoveMenuItemCommand(
            restaurantId = restaurantId.value.toString(),
            menuItemId = MenuItemId().value.toString()
        )

        val result = removeMenuItemUseCase.execute(command)

        assertTrue(result is Result.Failure)
        assertEquals("Menu item doesnt exist in the menu", (result as Result.Failure).error)
    }

    @Test
    fun `menu still has other items after removal`() {
        val secondItem = MenuItem.from("Pepperoni", "Pizza", Money.from(14.99))
        menu.addMenuItem(secondItem)
        menuRepository.save(menu)

        val command = RemoveMenuItemCommand(
            restaurantId = restaurantId.value.toString(),
            menuItemId = menuItem.id.value.toString()
        )

        val result = removeMenuItemUseCase.execute(command)

        assertTrue(result is Result.Success)
        val menuFromDb = menuRepository.findByRestaurantId(restaurantId)!!
        assertEquals(1, menuFromDb.getAllMenuItems().size)
        assertEquals("Pepperoni", menuFromDb.getAllMenuItems().first().name)
    }
}