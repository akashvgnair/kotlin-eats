package com.akashvgnair.kotlinEats.ports

import com.akashvgnair.kotlinEats.models.Restaurant
import com.akashvgnair.kotlinEats.models.RestaurantId
import com.akashvgnair.kotlinEats.repository.Repository

interface RestaurantRepository: Repository<Restaurant, RestaurantId>