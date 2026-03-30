package com.akashvgnair.kotlinEats.specification

class NotSpecification<T>(private val specification: Specification<T>) : Specification<T> {
    override fun isSatisfiedBy(candidate: T): Boolean {
        return !specification.isSatisfiedBy(candidate)
    }
}