package transaction

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OpenTransactionTest {
    private var transaction = OpenTransaction(CommittedDatabase())

    @BeforeEach
    fun setUp() {
        val database = CommittedDatabase()
        database.set("name", "Nicole")
        database.set("a", "foo")
        database.set("b", "bar")
        transaction = OpenTransaction(database)
    }

    @Test
    fun getsAVariableFromDelegate() {
        assertEquals("Nicole", transaction.get("name"))
        assertEquals(1, transaction.count("Nicole"))
    }

    @Test
    fun setsAVariable() {
        transaction.set("c", "Nicole")
        assertEquals("Nicole", transaction.get("c"))
        assertEquals(2, transaction.count("Nicole"))
    }

    @Test
    fun overridesAVariable() {
        transaction.set("name", "Danielle")
        assertEquals("Danielle", transaction.get("name"))
        assertEquals(0, transaction.count("Nicole"))
        assertEquals(1, transaction.count("Danielle"))
    }

    @Test
    fun getNullIfVariableNotSet() {
        assertNull( transaction.get("empty"))
        assertEquals( 0, transaction.count("empty"))
    }

    @Test
    fun deletesAVariable() {
        transaction.delete("name")
        assertNull(transaction.get("name"))
        assertEquals(0, transaction.count("Nicole"))
    }

    @Test
    fun deleteAnEmptyVariable() {
        transaction.delete("empty")
        assertNull(transaction.get("empty"))
        assertEquals(0, transaction.count("empty"))
    }

    @Test
    fun countIncrements() {
        transaction.set("b", "Nicole")
        transaction.set("c", "Nicole")
        assertEquals(3, transaction.count("Nicole"))

        transaction.delete("b")
        transaction.set("name", "Danielle")
        assertEquals(1, transaction.count("Nicole"))
    }

    @Test
    fun multipleNestedTransactions() {
        transaction.set("c", "baz")
        transaction.delete("b")
        val newerTransaction = OpenTransaction(transaction)

        newerTransaction.set("d", "hello")
        newerTransaction.delete("name")

        assertEquals("foo", newerTransaction.get("a"))
        assertNull(newerTransaction.get("b"))
        assertEquals("baz", newerTransaction.get("c"))
        assertEquals("hello", newerTransaction.get("d"))
        assertNull(newerTransaction.get("name"))

        assertEquals(0, newerTransaction.count("Nicole"))
        assertEquals(1, newerTransaction.count("hello"))
        assertEquals(1, newerTransaction.count("foo"))
    }
}
