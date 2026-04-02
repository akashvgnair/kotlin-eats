package com.akashvgnair.kotlinEats.ports

import com.akashvgnair.kotlinEats.models.Menu
import com.akashvgnair.kotlinEats.models.MenuId
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.repository.Repository

interface MenuRepository : Repository<Menu, MenuId> {
    fun findByRestaurantId(restaurantId: RestaurantId): Menu?
}