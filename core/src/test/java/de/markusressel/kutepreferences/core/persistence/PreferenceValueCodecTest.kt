package de.markusressel.kutepreferences.core.persistence

import de.markusressel.kutepreferences.core.preference.number.range.RangePersistenceModel
import de.markusressel.kutepreferences.core.preference.time.TimePersistenceModel
import org.junit.Assert.assertEquals
import org.junit.Test

class PreferenceValueCodecTest {

    private fun <T : Any> decode(json: String, default: T) = PreferenceValueCodec.decodeOrDefault("key", json, default)

    @Test
    fun `persisted format stays compatible`() {
        assertEquals("""{"hourOfDay":13,"minute":37}""", PreferenceValueCodec.encode(TimePersistenceModel(13, 37)))
        assertEquals("""{"min":1.5,"max":2.5}""", PreferenceValueCodec.encode(RangePersistenceModel(1.5f, 2.5f)))
        assertEquals("""["a","b"]""", PreferenceValueCodec.encode(setOf("a", "b")))
    }

    @Test
    fun `set elements keep the type of the default value`() {
        assertEquals(setOf(1, 2), decode("[1,2]", setOf(3)))
        assertEquals(setOf(1, 2), decode("[1,2]", setOf(3, 4)))
    }

    @Test
    fun `set with an empty default uses the json type`() {
        assertEquals(setOf("a"), decode("""["a"]""", emptySet<String>()))
    }

    @Test
    fun `set keeps its order`() {
        assertEquals(listOf("b", "a"), decode("""["b","a"]""", setOf("c")).toList())
    }

    @Test
    fun `range values keep the type of the default value`() {
        val range = decode("""{"min":1.0,"max":2.0}""", RangePersistenceModel(0f, 1f))

        assertEquals(RangePersistenceModel(1f, 2f), range)
    }

    @Test
    fun `an int range reads whole numbers and values that were saved as floats`() {
        assertEquals("""{"min":1,"max":2}""", PreferenceValueCodec.encode(RangePersistenceModel(1L, 2L)))
        assertEquals(RangePersistenceModel(1L, 2L), decode("""{"min":1,"max":2}""", RangePersistenceModel(0L, 1L)))
        assertEquals(RangePersistenceModel(1L, 2L), decode("""{"min":1.0,"max":2.5}""", RangePersistenceModel(0L, 1L)))
    }

    @Test
    fun `invalid json falls back to the default`() {
        val default = TimePersistenceModel(1, 2)

        assertEquals(default, decode("not json{", default))
        assertEquals(setOf("a"), decode("{}", setOf("a")))
        assertEquals(RangePersistenceModel(0f, 1f), decode("[]", RangePersistenceModel(0f, 1f)))
    }
}
