package com.akashvgnair.kotlinEats.usecase

import com.akashvgnair.kotlinEats.dto.AddMenuItemCommand
import com.akashvgnair.kotlinEats.events.EventBus
import com.akashvgnair.kotlinEats.models.Menu
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.ports.MenuRepository
import com.akashvgnair.kotlinEats.types.Result
import com.akashvgnair.kotlinEats.usecase.fixtures.MockEventBus
import com.akashvgnair.kotlinEats.usecase.fixtures.MockMenuRepository
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AddMenuItemUseCaseTest {
    val restaurantId = RestaurantId()
    val menu = Menu.from(restaurantId)
    lateinit var menuRepository: MenuRepository
    lateinit var eventBus: EventBus
    lateinit var addMenuItemUseCase: AddMenuItemUseCase

    fun createMenuItem(
        restaurantId: String,
        name: String = "mock menu item",
        category: String = "mock category",
        price: Double = 10.2,
        currency: String = "EUR"
    ): AddMenuItemCommand {
        return AddMenuItemCommand(
            restaurantId = restaurantId,
            name = name,
            category = category,
            price = price,
            currency = currency
        )
    }


    @BeforeEach
    fun setup() {
        menuRepository = MockMenuRepository()
        eventBus = MockEventBus()
        menuRepository.save(menu)

        addMenuItemUseCase = AddMenuItemUseCase(menuRepository, eventBus)
    }

    @Test
    fun `Add a new menu Item throws error for incorrect restaurant`() {
        val restaurantNotInDb = RestaurantId().value.toString()
        val menuItemCommand = createMenuItem(restaurantNotInDb)

        val result = addMenuItemUseCase.execute(menuItemCommand)

        assertTrue {
            result is Result.Failure
        }

        when (result) {
            is Result.Failure<*> -> assertEquals("Menu not found", result.error)
            is Result.Success<*> -> return
        }
    }

    @Test
    fun `Add a new menu Item throws error for blank menu item name`() {
        val command = createMenuItem(restaurantId.value.toString(), "")
        val result = addMenuItemUseCase.execute(command)

        assertTrue {
            result is Result.Failure
        }

        when (result) {
            is Result.Failure<*> -> assertEquals("Name of the menu item cannot be blank", result.error)
            is Result.Success<*> -> return
        }
    }

    @Test
    fun `Add a new menu Item throws error for blank menu item category`() {
        val command = createMenuItem(restaurantId.value.toString(), "some name", "")
        val result = addMenuItemUseCase.execute(command)

        assertTrue {
            result is Result.Failure
        }

        when (result) {
            is Result.Failure<*> -> assertEquals("Category of the menu item cannot be blank", result.error)
            is Result.Success<*> -> return
        }
    }

    @Test
    fun `Add a new menu Item throws error for negative menu item price`() {
        val command = createMenuItem(restaurantId.value.toString(), "some name", "cat", -1.2)
        val result = addMenuItemUseCase.execute(command)

        assertTrue {
            result is Result.Failure
        }

        when (result) {
            is Result.Failure<*> -> assertEquals("Value should be greater than or equal to 0", result.error)
            is Result.Success<*> -> return
        }
    }

    @Test
    fun `Add a new menu Item throws error for zero menu item price`() {
        val command = createMenuItem(restaurantId.value.toString(), "some name", "cat", 0.0)
        val result = addMenuItemUseCase.execute(command)

        assertTrue {
            result is Result.Failure
        }

        when (result) {
            is Result.Failure<*> -> assertEquals("Price of the menu item should be positive", result.error)
            is Result.Success<*> -> return
        }
    }

    @Test
    fun `Add a new menu Item throws error for invalid menu item price currency`() {
        val command = createMenuItem(restaurantId.value.toString(), "some name", "cat", 10.0, "INVALID")
        val result = addMenuItemUseCase.execute(command)

        assertTrue {
            result is Result.Failure
        }

        when (result) {
            is Result.Failure<*> -> assertEquals("Menu item couldn't be saved", result.error)
            is Result.Success<*> -> return
        }
    }

    @Test
    fun `Add a new menu Item`() {
        val menuItemCommand = createMenuItem(restaurantId.value.toString())
        val result = addMenuItemUseCase.execute(menuItemCommand)

        assertTrue {
            result is Result.Success
        }

        when (result) {
            is Result.Failure<*> -> assertEquals("Menu not found", result.error)
            is Result.Success<*> -> {
                val menuItemFromResult = result.getOrNull()!!
                assertNotNull(menuItemFromResult)

                val menuFromDb = menuRepository.findByRestaurantId(restaurantId)!!
                val menuItemFromDb = menuFromDb.getMenuItemByMenuItemId(menuItemFromResult.id)

                assertEquals(menuItemFromDb, menuItemFromResult)
            }
        }
    }


}