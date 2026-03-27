package com.akashvgnair.kotlinEats.types

class Address private constructor(
    val street: String, val city: String, val postalCode: String, val country: String, val additionalInfo: String
) {

    companion object {
        private fun requireNonBlank(value: String) {
            require(value.isNotBlank()) {
                "Value should not be blank"
            }
        }

        fun from(
            street: String,
            city: String,
            postalCode: String,
            country: String,
            additionalInfo: String = ""
        ): Address {
            requireNonBlank(street)
            requireNonBlank(city)
            requireNonBlank(postalCode)
            requireNonBlank(country)
            return Address(street, city, postalCode, country, additionalInfo)
        }
    }

    override fun toString(): String {
        val base = "$street, $city, $postalCode, $country"
        return if (additionalInfo.isBlank()) base else "$base ($additionalInfo)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Address

        if (street != other.street) return false
        if (city != other.city) return false
        if (postalCode != other.postalCode) return false
        if (country != other.country) return false
        if (additionalInfo != other.additionalInfo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = street.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + postalCode.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + additionalInfo.hashCode()
        return result
    }
}