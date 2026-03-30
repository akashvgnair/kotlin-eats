package com.akashvgnair.kotlinEats.specification

interface Specification<T> {
    fun isSatisfiedBy(candidate: T): Boolean

    infix fun and(other: Specification<T>) = AndSpecification(this, other)
    infix fun or(other: Specification<T>) = OrSpecification(this, other)
    operator fun not() = NotSpecification(this)
}