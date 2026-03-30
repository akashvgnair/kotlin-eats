package com.akashvgnair.kotlinEats.types

abstract class AggregateRoot<ID : EntityId>(val id: ID) {
    private val domainEvents: MutableList<DomainEvent> = mutableListOf()

    fun getDomainEvents(): List<DomainEvent> = domainEvents.toList()

    fun addEvent(event: DomainEvent) {
        domainEvents.add(event)
    }

    fun clearEvents(): List<DomainEvent> {
        val temp = domainEvents.toList()
        domainEvents.clear()
        return temp
    }

    override fun toString(): String {
        return "AggregateRoot<${javaClass.simpleName}> = ${id.value})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AggregateRoot<*>

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}