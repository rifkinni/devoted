import instruction.Instruction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TransactionManagerTest {
    private val outputStreamCaptor = ByteArrayOutputStream()
    private var manager = TransactionManager()

    @BeforeEach
    fun setUp() {
        manager = TransactionManager()
        System.setOut(PrintStream(outputStreamCaptor))
    }

    @AfterEach
    fun tearDown() {
        outputStreamCaptor.reset()
    }
    @Test
    fun noOpenTransactions() {
        manager.execute(Instruction("SET name Nicole"))
        manager.execute(Instruction("GET name"))
        assertEquals("Nicole", outputStreamCaptor.toString().trim())
    }

    @Test
    fun rollbackNoop() {
        manager.execute(Instruction("SET name Nicole"))
        manager.execute(Instruction("ROLLBACK"))
        assertEquals("TRANSACTION NOT FOUND", outputStreamCaptor.toString().trim())
        outputStreamCaptor.reset()

        manager.execute(Instruction("GET name"))
        assertEquals("Nicole", outputStreamCaptor.toString().trim())
    }

    @Test
    fun rollsBackOpenTransaction() {
        manager.execute(Instruction("BEGIN"))
        manager.execute(Instruction("SET transactionId 1"))
        manager.execute(Instruction("GET transactionId"))
        manager.execute(Instruction("ROLLBACK"))
        manager.execute(Instruction("GET transactionId"))

        val expected = listOf("1", "null")
        assertEquals(expected, outputStreamCaptor.toString().trim().split("\n"))
    }

    @Test
    fun rollsBackOnlyOneTransaction() {
        manager.execute(Instruction("BEGIN"))
        manager.execute(Instruction("SET transactionId 1"))

        manager.execute(Instruction("BEGIN"))
        manager.execute(Instruction("SET transactionId 2"))
        manager.execute(Instruction("GET transactionId"))

        manager.execute(Instruction("ROLLBACK"))
        manager.execute(Instruction("GET transactionId"))

        val expected = listOf("2", "1")
        assertEquals(expected, outputStreamCaptor.toString().trim().split("\n"))
    }

    @Test
    fun commitsAllOpenTransactions() {
        manager.execute(Instruction("BEGIN"))
        manager.execute(Instruction("SET transactionId 1"))

        manager.execute(Instruction("BEGIN"))
        manager.execute(Instruction("SET transactionId 2"))
        manager.execute(Instruction("COMMIT"))
        manager.execute(Instruction("ROLLBACK"))
        assertEquals("TRANSACTION NOT FOUND", outputStreamCaptor.toString().trim())
        outputStreamCaptor.reset()

        manager.execute(Instruction("GET transactionId"))
        assertEquals("2", outputStreamCaptor.toString().trim())
    }
}
