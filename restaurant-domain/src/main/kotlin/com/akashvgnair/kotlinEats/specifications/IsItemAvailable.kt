package com.akashvgnair.kotlinEats.specifications

import com.akashvgnair.kotlinEats.models.Menu
import com.akashvgnair.kotlinEats.models.MenuItem
import com.akashvgnair.kotlinEats.specification.Specification

class IsItemAvailable(val item: MenuItem) : Specification<Menu> {
    override fun isSatisfiedBy(candidate: Menu): Boolean {
        return candidate.getAvailableItems().any {
            it.id == item.id && it.available
        }
    }
}