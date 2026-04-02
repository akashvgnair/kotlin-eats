package com.akashvgnair.kotlinEats.models

import com.akashvgnair.kotlinEats.types.EntityId
import java.util.UUID

class RestaurantId(id: UUID = UUID.randomUUID()) : EntityId(id)