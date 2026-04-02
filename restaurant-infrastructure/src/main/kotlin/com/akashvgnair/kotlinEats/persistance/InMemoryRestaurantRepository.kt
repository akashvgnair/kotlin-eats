package com.akashvgnair.kotlinEats.persistance

import com.akashvgnair.kotlinEats.models.Restaurant
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.ports.RestaurantRepository

object InMemoryRestaurantRepository : RestaurantRepository {
    private val restaurantsMap: MutableMap<RestaurantId, Restaurant> = mutableMapOf()

    override fun findById(id: RestaurantId): Restaurant? {
        return restaurantsMap[id]
    }

    override fun save(aggregate: Restaurant) {
        restaurantsMap[aggregate.id] = aggregate
    }

    override fun delete(id: RestaurantId): Boolean {
        return restaurantsMap.remove(id) != null
    }

    override fun findAll(): List<Restaurant> {
        return restaurantsMap.values.toList()
    }
}