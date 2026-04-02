package com.akashvgnair.kotlinEats.dto

data class AddMenuItemCommand(
    val restaurantId: String,
    val name: String,
    val category: String,
    val price: Double,
    val currency: String
)
