package integration

import instruction.Instruction
import TransactionManager
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class DatabaseIntegrationTest {
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
    fun testExample1() {
        File("src/test/kotlin/integration/fixtures/example1.txt").forEachLine {
            manager.execute(Instruction(it))
        }
        val expected = listOf("null", "2", "0", "1", "0", "baz", "null")
        val actual = outputStreamCaptor.toString().trim().split("\n")
        assertEquals(expected, actual)
    }

    @Test
    fun testExample2() {
        File("src/test/kotlin/integration/fixtures/example2.txt").forEachLine {
            manager.execute(Instruction(it))
        }
        val expected = listOf("1", "foo", "null", "0")
        val actual = outputStreamCaptor.toString().trim().split("\n")
        assertEquals(expected, actual)
    }

    @Test
    fun testExample3() {
        File("src/test/kotlin/integration/fixtures/example3.txt").forEachLine {
            manager.execute(Instruction(it))
        }
        val expected = listOf("foo", "bar", "foo", "null")
        val actual = outputStreamCaptor.toString().trim().split("\n")
        assertEquals(expected, actual)
    }

    @Test
    fun testExample4() {
        File("src/test/kotlin/integration/fixtures/example4.txt").forEachLine {
            manager.execute(Instruction(it))
        }
        val expected = listOf("foo", "1", "1", "null", "0", "bar", "1", "bar", "baz")
        val actual = outputStreamCaptor.toString().trim().split("\n")
        assertEquals(expected, actual)
    }
}
