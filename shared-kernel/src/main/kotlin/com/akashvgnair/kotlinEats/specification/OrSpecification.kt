package com.akashvgnair.kotlinEats.specification

class OrSpecification<T>(private val left: Specification<T>, private val right: Specification<T>): Specification<T> {
    override fun isSatisfiedBy(candidate: T): Boolean {
        return left.isSatisfiedBy(candidate) || right.isSatisfiedBy(candidate)
    }
}