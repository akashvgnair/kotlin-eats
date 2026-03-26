package com.akashvgnair.kotlinEats.types

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class TestId(value: UUID) : EntityId(value)
class OtherId(value: UUID) : EntityId(value)

class EntityIdTest {

    @Test
    fun `Create with a UUID`() {
        val uuid = UUID.randomUUID()
        val id = TestId(uuid)
        assertEquals(uuid, id.value)
    }

    @Test
    fun `Two same uuid returns same id`() {
        val uuid = UUID.randomUUID()
        val id1 = TestId(uuid)
        val id2 = TestId(uuid)
        assertEquals(id1, id2)
    }

    @Test
    fun `Two different uuid returns different id`() {
        val id1 = TestId(UUID.randomUUID())
        val id2 = TestId(UUID.randomUUID())
        assertNotEquals(id1, id2)
    }

    @Test
    fun `different types with same UUID are not equal`() {
        val uuid = UUID.randomUUID()
        assertNotEquals<EntityId>(TestId(uuid), OtherId(uuid))
    }

    @Test
    fun `toString shows class name and UUID`() {
        val uuid = UUID.randomUUID()
        val id = TestId(uuid)
        assertEquals("TestId($uuid)", id.toString())
    }
}