package transaction

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CommittedDatabaseTest {
    private var database = CommittedDatabase()

    @BeforeEach
    fun setUp() {
        database = CommittedDatabase()
    }

    @Test
    fun setsAVariable() {
        database.set("name", "Nicole")
        assertEquals("Nicole", database.get("name"))
        assertEquals(1, database.count("Nicole"))
    }

    @Test
    fun overridesAVariable() {
        database.set("name", "Nicole")
        database.set("name", "Danielle")
        assertEquals("Danielle", database.get("name"))
        assertEquals(0, database.count("Nicole"))
        assertEquals(1, database.count("Danielle"))
    }

    @Test
    fun getNullIfVariableNotSet() {
        assertNull( database.get("foo"))
        assertEquals( 0, database.count("foo"))
    }

    @Test
    fun deletesAVariable() {
        database.set("name", "Nicole")
        assertEquals("Nicole", database.get("name"))
        assertEquals(1, database.count("Nicole"))

        database.delete("name")
        assertNull(database.get("name"))
        assertEquals(0, database.count("Nicole"))
    }

    @Test
    fun deleteAnEmptyVariable() {
        database.delete("foo")
        assertNull(database.get("foo"))
        assertEquals(0, database.count("foo"))
    }

    @Test
    fun countIncrements() {
        database.set("a", "Nicole")
        assertEquals(1, database.count("Nicole"))

        database.set("b", "Nicole")
        database.set("c", "Nicole")
        assertEquals(3, database.count("Nicole"))

        database.delete("b")
        database.set("a", "Danielle")
        assertEquals(1, database.count("Nicole"))
    }

    @Test
    fun mergesATransactionState() {
        database.set("a", "Nicole")
        database.set("b", "Danielle")

        val openTransaction = OpenTransaction(database)
        openTransaction.set("a", "foo")
        openTransaction.delete("b")

        database.commit(openTransaction)
        assertEquals("foo", database.get("a"))
        assertNull(database.get("b"))
        assertEquals(1, database.count("foo"))
        assertEquals(0, database.count("Nicole"))
        assertEquals(0, database.count("Danielle"))
    }
}
