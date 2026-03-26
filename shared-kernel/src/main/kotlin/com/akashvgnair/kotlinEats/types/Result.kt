package com.akashvgnair.kotlinEats.types

sealed class Result<out T, out E> {
    data class Success<T>(val value: T) : Result<T, Nothing>()
    data class Failure<E>(val error: E) : Result<Nothing, E>()

    fun <R> map(transform: (T) -> R): Result<R, E> = when (this) {
        is Success -> Success(transform(value))
        is Failure -> Failure(error)
    }

    fun <R> flatMap(transform: (T) -> Result<R, @UnsafeVariance E>): Result<R, E> = when (this) {
        is Success -> transform(value)
        is Failure -> Failure(error)
    }

    fun <F> mapError(transform: (E) -> F): Result<T, F> = when (this) {
        is Success -> Success(value)
        is Failure -> Failure(transform(error))
    }

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    fun getOrElse(default: () -> @UnsafeVariance T): T = when (this) {
        is Success -> value
        is Failure -> default()
    }
}