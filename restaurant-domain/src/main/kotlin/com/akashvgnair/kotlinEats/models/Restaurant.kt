package com.akashvgnair.kotlinEats.models

import com.akashvgnair.kotlinEats.events.RestaurantEvents
import com.akashvgnair.kotlinEats.types.Address
import com.akashvgnair.kotlinEats.types.AggregateRoot
import java.time.LocalTime

class Restaurant private constructor(
    id: RestaurantId,
    initialName: String,
    initialAddress: Address,
    initialCuisineType: CuisineType,
    initialOperatingHours: OperatingHours,
    initialStatus: RestaurantStatus
) : AggregateRoot<RestaurantId>(id) {

    init {
        this.addEvent(RestaurantEvents.RestaurantCreatedEvent(this))
    }

    var status = initialStatus
        private set

    var operatingHours = initialOperatingHours
        private set

    var name = initialName
        private set

    var address = initialAddress
        private set

    var cuisineType = initialCuisineType
        private set

    fun setName(newName: String) {
        require(newName.isNotBlank()) {
            RESTAURANT_NAME_NOT_BLANK
        }
        name = newName
    }

    fun open() {
        require(status != RestaurantStatus.OPEN) { "Restaurant is already open" }
        status = RestaurantStatus.OPEN
        addEvent(RestaurantEvents.RestaurantStatusChangedEvent(this))
    }

    fun close() {
        require(status != RestaurantStatus.CLOSED) { "Restaurant is already closed" }
        status = RestaurantStatus.CLOSED
        addEvent(RestaurantEvents.RestaurantStatusChangedEvent(this))
    }

    fun setAddress(newAddress: Address) {
        address = newAddress
    }

    fun setCuisineType(newCuisineType: CuisineType) {
        cuisineType = newCuisineType
    }

    fun setOperatingHours(newOperatingHours: OperatingHours) {
        operatingHours = newOperatingHours
    }

    fun isOpen(time: LocalTime): Boolean = status == RestaurantStatus.OPEN && operatingHours.isTimeWithinHours(time)

    companion object {
        private const val RESTAURANT_NAME_NOT_BLANK = "Restaurant name cannot be blank"
        fun from(
            name: String,
            address: Address,
            cuisineType: CuisineType,
            operatingHours: OperatingHours,
        ): Restaurant {
            require(name.isNotBlank()) {
                RESTAURANT_NAME_NOT_BLANK
            }
            val id = RestaurantId()

            return Restaurant(id, name, address, cuisineType, operatingHours, RestaurantStatus.CLOSED)
        }
    }
}