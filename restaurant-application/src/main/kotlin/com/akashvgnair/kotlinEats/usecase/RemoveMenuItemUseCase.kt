package com.akashvgnair.kotlinEats.usecase

import com.akashvgnair.kotlinEats.dto.RemoveMenuItemCommand
import com.akashvgnair.kotlinEats.events.EventBus
import com.akashvgnair.kotlinEats.models.MenuItem
import com.akashvgnair.kotlinEats.models.MenuItemId
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.ports.MenuRepository
import com.akashvgnair.kotlinEats.types.Result
import com.akashvgnair.kotlinEats.types.UseCase
import java.util.UUID

class RemoveMenuItemUseCase(private val menuRepository: MenuRepository, private val eventBus: EventBus) :
    UseCase<RemoveMenuItemCommand, Result<MenuItem, String>> {
    override fun execute(command: RemoveMenuItemCommand): Result<MenuItem, String> {
        val menu = menuRepository.findByRestaurantId(RestaurantId(UUID.fromString(command.restaurantId)))
            ?: return Result.Failure("Menu not found for the restaurant")

        try {
            val menuItemId = MenuItemId(UUID.fromString(command.menuItemId))
            val menuItem =
                menu.getMenuItemByMenuItemId(menuItemId) ?: return Result.Failure("Menu item doesnt exist in the menu")
            menu.removeMenuItem(menuItemId)

            menuRepository.save(menu)
            eventBus.publishAll(menu.clearEvents())
            return Result.Success(menuItem)


        } catch (e: Exception) {
            return Result.Failure(e.message ?: "Failed to delete the menu item")
        }

    }
}