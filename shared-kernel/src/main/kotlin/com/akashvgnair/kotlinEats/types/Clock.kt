package com.akashvgnair.kotlinEats.types

import java.time.Instant

interface Clock {
    fun now(): Instant
}