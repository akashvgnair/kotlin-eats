package com.akashvgnair.kotlinEats.types

import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

val id1: UUID = UUID.fromString("a07f8fbb-7b1f-4e99-aa94-37114b5946de")
val id2: UUID = UUID.fromString("a07f8fbb-7b1f-4e99-aa94-37114b5946df")

class BaseEntityId(id: UUID) : EntityId(id)


val baseEntityId1 = BaseEntityId(id1)
val baseEntityId2 = BaseEntityId(id2)


class BaseEntity(id: BaseEntityId, val name: String) : Entity<BaseEntityId>(id)

val entity1 = BaseEntity(baseEntityId1, "red")
val entity2 = BaseEntity(baseEntityId1, "green")
val entity3 = BaseEntity(baseEntityId2, "red")


class EntityTest {

    @Test
    fun `Two entities with same Id are equal`() {
        assertEquals(entity1, entity2)
    }

    @Test
    fun `Two entities with different Id are different`() {
        assertNotEquals(entity1, entity3)
    }

    @Test
    fun `Read all properties of entity`() {
        assertEquals(baseEntityId1, entity1.id)
        assertEquals("red", entity1.name)
    }

    @Test
    fun `prints entity`() {
        assertEquals("Entity<BaseEntity> = ${baseEntityId1.value})", entity1.toString())
    }
}