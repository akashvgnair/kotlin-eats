package com.akashvgnair.kotlinEats.models

import com.akashvgnair.kotlinEats.types.Entity
import com.akashvgnair.kotlinEats.types.Money

class MenuItem private constructor(
    id: MenuItemId,
    initialName: String,
    initialCategory: String,
    initialPrice: Money,
    isAvailableInitially: Boolean
) :
    Entity<MenuItemId>(id) {

    var name = initialName
        private set
    var category = initialCategory
        private set
    var price = initialPrice
        private set
    var available = isAvailableInitially
        private set

    fun setName(newName: String) {
        require(newName.isNotBlank()) {
            NAME_NOT_BLANK
        }
        name = newName
    }

    fun setCategory(newCategory: String) {
        require(newCategory.isNotBlank()) {
            CATEGORY_NOT_BLANK
        }
        category = newCategory
    }

    fun setPrice(newPrice: Money) {
        require(newPrice > Money.fromZero(price.currency)) {
            PRICE_POSITIVE
        }
        price = newPrice
    }

    fun setAvailable(isAvailable: Boolean) {
        available = isAvailable
    }

    override fun toString(): String {
        return "MenuItem(id='$id', name='$name', category='$category', price=$price, available=$available)"
    }

    companion object {
        private const val NAME_NOT_BLANK = "Name of the menu item cannot be blank"
        private const val CATEGORY_NOT_BLANK = "Category of the menu item cannot be blank"
        private const val PRICE_POSITIVE = "Price of the menu item should be positive"
        fun from(name: String, category: String, price: Money): MenuItem {
            val menuItemId = MenuItemId()
            require(name.isNotBlank()) {
                NAME_NOT_BLANK
            }

            require(category.isNotBlank()) {
                CATEGORY_NOT_BLANK
            }

            require(price > Money.fromZero(price.currency)) {
                PRICE_POSITIVE
            }

            return MenuItem(menuItemId, name, category, price, true)
        }
    }

}