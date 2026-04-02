package com.akashvgnair.kotlinEats.usecase.fixtures

import com.akashvgnair.kotlinEats.models.Menu
import com.akashvgnair.kotlinEats.models.MenuId
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.ports.MenuRepository

class MockMenuRepository() : MenuRepository {
    private val menuMap: MutableMap<MenuId, Menu> = mutableMapOf()

    override fun findByRestaurantId(restaurantId: RestaurantId): Menu? {
        return menuMap.values.find { it.restaurantId == restaurantId }
    }

    override fun findById(id: MenuId): Menu? {
        return menuMap[id]
    }

    override fun save(aggregate: Menu) {
        menuMap[aggregate.id] = aggregate
    }

    override fun delete(id: MenuId): Boolean {
        return menuMap.remove(id) != null
    }

    override fun findAll(): List<Menu> {
        return menuMap.values.toList()
    }
}
