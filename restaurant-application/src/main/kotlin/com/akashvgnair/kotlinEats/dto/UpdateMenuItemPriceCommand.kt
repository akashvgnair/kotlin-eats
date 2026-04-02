package com.akashvgnair.kotlinEats.dto

data class UpdateMenuItemPriceCommand(
    val restaurantId: String,
    val menuItemId: String,
    val price: Double,
    val currency: String
)
