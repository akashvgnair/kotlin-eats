package com.akashvgnair.kotlinEats.usecase

import com.akashvgnair.kotlinEats.dto.AddMenuItemCommand
import com.akashvgnair.kotlinEats.events.EventBus
import com.akashvgnair.kotlinEats.models.MenuItem
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.ports.MenuRepository
import com.akashvgnair.kotlinEats.types.Money
import com.akashvgnair.kotlinEats.types.Result
import com.akashvgnair.kotlinEats.types.UseCase
import java.util.Currency
import java.util.UUID

class AddMenuItemUseCase(
    private val menuRepository: MenuRepository,
    private val eventBus: EventBus
) :
    UseCase<AddMenuItemCommand, Result<MenuItem, String>> {
    override fun execute(command: AddMenuItemCommand): Result<MenuItem, String> {
        val menu = menuRepository.findByRestaurantId(RestaurantId(UUID.fromString(command.restaurantId)))
            ?: return Result.Failure("Menu not found")

        try {
            val menuItem = MenuItem.from(
                name = command.name,
                category = command.category,
                price = Money.from(command.price, Currency.getInstance(command.currency))
            )

            menu.addMenuItem(menuItem)
            menuRepository.save(menu)
            eventBus.publishAll(menu.clearEvents())

            return Result.Success(menuItem)
        } catch (e: Exception) {
            return Result.Failure(e.message ?: "Menu item couldn't be saved")
        }
    }
}