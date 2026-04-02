package com.akashvgnair.kotlinEats.specifications

import com.akashvgnair.kotlinEats.models.Restaurant
import com.akashvgnair.kotlinEats.specification.Specification
import java.time.LocalTime

class IsRestaurantOpen(val time: LocalTime) : Specification<Restaurant> {
    override fun isSatisfiedBy(candidate: Restaurant): Boolean {
        return candidate.isOpen(time)
    }
}