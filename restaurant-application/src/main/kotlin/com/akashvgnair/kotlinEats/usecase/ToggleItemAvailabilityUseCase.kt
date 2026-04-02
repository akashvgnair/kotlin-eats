package com.akashvgnair.kotlinEats.usecase

import com.akashvgnair.kotlinEats.dto.ToggleItemAvailabilityCommand
import com.akashvgnair.kotlinEats.events.EventBus
import com.akashvgnair.kotlinEats.models.MenuItem
import com.akashvgnair.kotlinEats.models.MenuItemId
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.ports.MenuRepository
import com.akashvgnair.kotlinEats.types.Result
import com.akashvgnair.kotlinEats.types.UseCase
import java.util.UUID

class ToggleItemAvailabilityUseCase(private val menuRepository: MenuRepository, private val eventBus: EventBus) :
    UseCase<ToggleItemAvailabilityCommand, Result<MenuItem, String>> {
    override fun execute(command: ToggleItemAvailabilityCommand): Result<MenuItem, String> {
        try {
            val restaurantId = RestaurantId(UUID.fromString(command.restaurantId))
            val menu = menuRepository.findByRestaurantId(restaurantId)
                ?: return Result.Failure("Menu not found for the restaurant")

            val menuItem = menu.getMenuItemByMenuItemId(MenuItemId(UUID.fromString(command.menuItemId)))
                ?: return Result.Failure("Menu item not found in the menu")

            menu.toggleMenuItemAvailability(menuItem.id)
            menuRepository.save(menu)
            eventBus.publishAll(menu.clearEvents())
            return Result.Success(menuItem)

        } catch (e: Exception) {
            return Result.Failure(e.message ?: "Failed to update the price of the menu item")
        }
    }
}