package com.akashvgnair.kotlinEats.usecase

import com.akashvgnair.kotlinEats.dto.CreateRestaurantCommand
import com.akashvgnair.kotlinEats.events.EventBus
import com.akashvgnair.kotlinEats.models.CuisineType
import com.akashvgnair.kotlinEats.models.Menu
import com.akashvgnair.kotlinEats.models.OperatingHours
import com.akashvgnair.kotlinEats.models.Restaurant
import com.akashvgnair.kotlinEats.ports.MenuRepository
import com.akashvgnair.kotlinEats.ports.RestaurantRepository
import com.akashvgnair.kotlinEats.types.Address
import com.akashvgnair.kotlinEats.types.Result
import com.akashvgnair.kotlinEats.types.UseCase

class CreateRestaurantUseCase(
    private val restaurantRepository: RestaurantRepository,
    private val menuRepository: MenuRepository,
    private val eventBus: EventBus
) : UseCase<CreateRestaurantCommand, Result<Restaurant, String>> {
    override fun execute(command: CreateRestaurantCommand): Result<Restaurant, String> {

        try {
            val address =
                Address.from(command.street, command.city, command.postalCode, command.country, command.additionalInfo)

            val cuisineType = CuisineType.valueOf(command.cuisineType)

            val operatingHours = OperatingHours.from(command.openTime, command.closeTime)

            val restaurant = Restaurant.from(
                name = command.name,
                address = address,
                cuisineType = cuisineType,
                operatingHours = operatingHours
            )

            restaurantRepository.save(restaurant)
            menuRepository.save(Menu.from(restaurant.id))
            eventBus.publishAll(restaurant.clearEvents())
            return Result.Success(restaurant)

        } catch (e: Exception) {
            return Result.Failure(e.message ?: "Restaurant could not be saved")
        }


    }

}