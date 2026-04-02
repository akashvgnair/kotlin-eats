package com.akashvgnair.kotlinEats.dto

import java.time.LocalTime

data class CreateRestaurantCommand(
    val name: String,
    val street: String,
    val city: String,
    val postalCode: String,
    val country: String,
    val additionalInfo: String,
    val cuisineType: String,
    val openTime: LocalTime,
    val closeTime: LocalTime
)
