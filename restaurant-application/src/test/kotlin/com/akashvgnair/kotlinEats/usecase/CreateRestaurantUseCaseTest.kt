package com.akashvgnair.kotlinEats.usecase

import com.akashvgnair.kotlinEats.dto.CreateRestaurantCommand
import com.akashvgnair.kotlinEats.events.EventBus
import com.akashvgnair.kotlinEats.ports.MenuRepository
import com.akashvgnair.kotlinEats.ports.RestaurantRepository
import com.akashvgnair.kotlinEats.types.Result
import com.akashvgnair.kotlinEats.usecase.fixtures.MockEventBus
import com.akashvgnair.kotlinEats.usecase.fixtures.MockMenuRepository
import com.akashvgnair.kotlinEats.usecase.fixtures.MockRestaurantRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalTime


class CreateRestaurantUseCaseTest {
    lateinit var restaurantRepository: RestaurantRepository
    lateinit var menuRepository: MenuRepository
    lateinit var createRestaurant: CreateRestaurantUseCase
    lateinit var eventBus: EventBus

    fun getRestaurantCommand(
        name: String = "mock name",
        city: String = "Mock city",
        cuisineType: String = "ITALIAN"
    ): CreateRestaurantCommand {
        return CreateRestaurantCommand(
            name = name,
            street = "21 street",
            city = city,
            postalCode = "24350",
            country = "Mock Country",
            additionalInfo = "Door no 45",
            cuisineType = cuisineType,
            openTime = LocalTime.of(9, 0),
            closeTime = LocalTime.of(18, 0)
        )
    }


    @BeforeEach
    fun setup() {
        restaurantRepository = MockRestaurantRepository()
        menuRepository = MockMenuRepository()
        eventBus = MockEventBus()

        createRestaurant = CreateRestaurantUseCase(
            restaurantRepository = restaurantRepository,
            menuRepository = menuRepository,
            eventBus = eventBus
        )

    }


    @Test
    fun `It creates a restaurant and saves it in the DB and the events are published`() {
        val command = getRestaurantCommand()
        val result = createRestaurant.execute(command)
        assertTrue {
            result is Result.Success
        }

        val savedRestaurant = restaurantRepository.findAll().first()

        assertEquals(command.name, savedRestaurant.name)
        assertEquals(result.getOrNull()?.id, savedRestaurant.id)
    }

    @Test
    fun `It fails when name is blank`() {
        val command = getRestaurantCommand("")
        val result = createRestaurant.execute(command)

        assertTrue {
            result is Result.Failure
        }

        when (result) {
            is Result.Failure<*> -> assertEquals("Restaurant name cannot be blank", result.error)
            is Result.Success<*> -> return
        }
    }

    @Test
    fun `It fails when address city is blank`() {
        val command = getRestaurantCommand("Some name", "")
        val result = createRestaurant.execute(command)

        assertTrue {
            result is Result.Failure
        }

        when (result) {
            is Result.Failure<*> -> assertEquals("Value should not be blank", result.error)
            is Result.Success<*> -> return
        }
    }

    @Test
    fun `It fails when cuisine is not from the allowed list`() {
        val command = getRestaurantCommand("Some name", "some city", "ASIAN")
        val result = createRestaurant.execute(command)

        assertTrue {
            result is Result.Failure
        }

        when (result) {
            is Result.Failure<*> -> assertEquals(
                "No enum constant com.akashvgnair.kotlinEats.models.CuisineType.ASIAN",
                result.error
            )

            is Result.Success<*> -> return
        }
    }

    @Test
    fun `menu is created for the restaurant`() {
        val command = getRestaurantCommand()
        val result = createRestaurant.execute(command)

        val restaurant = result.getOrNull()!!
        val menu = menuRepository.findAll().first()
        assertEquals(restaurant.id, menu.restaurantId)
    }

    @Test
    fun `nothing is saved when creation fails`() {
        val command = getRestaurantCommand(name = "")
        createRestaurant.execute(command)

        assertTrue(restaurantRepository.findAll().isEmpty())
        assertTrue(menuRepository.findAll().isEmpty())
    }
}