package com.akashvgnair.kotlinEats.repository

import com.akashvgnair.kotlinEats.types.AggregateRoot
import com.akashvgnair.kotlinEats.types.EntityId

interface Repository<T : AggregateRoot<ID>, ID : EntityId> {
    fun findById(id: ID): T?
    fun save(aggregate: T)
    fun delete(id: ID): Boolean
    fun findAll(): List<T>
    fun existsById(id: ID): Boolean = findById(id) != null
}