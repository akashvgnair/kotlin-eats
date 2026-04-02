package com.akashvgnair.kotlinEats.types

interface UseCase<in C, out R> {
    fun execute(command: C): R
}