package com.akashvgnair.kotlinEats.types

import java.util.UUID

abstract class EntityId(val value: UUID) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntityId

        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return "${this::class.simpleName}($value)"
    }
}