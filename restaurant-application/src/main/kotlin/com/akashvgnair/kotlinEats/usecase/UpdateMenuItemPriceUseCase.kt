package com.akashvgnair.kotlinEats.usecase

import com.akashvgnair.kotlinEats.dto.UpdateMenuItemPriceCommand
import com.akashvgnair.kotlinEats.events.EventBus
import com.akashvgnair.kotlinEats.models.MenuItem
import com.akashvgnair.kotlinEats.models.MenuItemId
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.ports.MenuRepository
import com.akashvgnair.kotlinEats.types.Money
import com.akashvgnair.kotlinEats.types.Result
import com.akashvgnair.kotlinEats.types.UseCase
import java.util.Currency
import java.util.UUID

class UpdateMenuItemPriceUseCase(private val menuRepository: MenuRepository, private val eventBus: EventBus) :
    UseCase<UpdateMenuItemPriceCommand, Result<MenuItem, String>> {
    override fun execute(command: UpdateMenuItemPriceCommand): Result<MenuItem, String> {

        try {
            val restaurantId = RestaurantId(UUID.fromString(command.restaurantId))
            val menu = menuRepository.findByRestaurantId(restaurantId)
                ?: return Result.Failure("Menu not found for the restaurant")

            val updatedPrice = Money.from(command.price, Currency.getInstance(command.currency))

            val menuItem = menu.getMenuItemByMenuItemId(MenuItemId(UUID.fromString(command.menuItemId)))
                ?: return Result.Failure("Menu item not found in the menu")

            menu.updateMenuItemPrice(menuItem.id, updatedPrice)
            menuRepository.save(menu)
            eventBus.publishAll(menu.clearEvents())
            return Result.Success(menuItem)

        } catch (e: Exception) {
            return Result.Failure(e.message ?: "Failed to update the price of the menu item")
        }

    }
}