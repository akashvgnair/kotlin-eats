package com.akashvgnair.kotlinEats.types

abstract class Entity<ID : EntityId>(val id: ID) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Entity<*>
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "Entity<${javaClass.simpleName}> = ${id.value})"
    }
}