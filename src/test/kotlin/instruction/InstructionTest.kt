package instruction

import instruction.Instruction
import instruction.InstructionType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class InstructionTest {
    @Test
    fun createsInstructionWithoutArgs() {
        val instruction = Instruction("BEGIN")
        assertEquals(instruction.type, InstructionType.BEGIN)
        assertEquals(instruction.args, emptyList<String>())
    }

    @Test
    fun createsInstructionWithArgs() {
        val instruction = Instruction("SET a 1")
        assertEquals(instruction.type, InstructionType.SET)
        assertEquals(instruction.args, listOf("a", "1"))
    }

    @ParameterizedTest
    @ValueSource(strings = [
        " SET name 1 ", " SET 1 name ",
        "GET name ", "GET 1",
        "DELETE name", "DELETE 1",
        "COUNT name", "COUNT 1",
        "BEGIN", "COMMIT", "ROLLBACK", "END"
    ])
    fun createsValidInstructions(input: String) {
        val instruction = Instruction(input)
        assertNotEquals(InstructionType.ERROR, instruction.type)
    }

    @ParameterizedTest
    @ValueSource(strings = [
        " ", "", "FOO",
        "SET name", "SET",
        "GET name 1", "GET",
        "DELETE name 1", "DELETE",
        "COUNT name 1", "COUNT",
        "BEGIN 1", "COMMIT 1", "ROLLBACK 1"
    ])
    fun invalidInstructionsDefaultToEnd(input: String) {
        val instruction = Instruction(input)
        assertEquals(InstructionType.ERROR, instruction.type)
    }
}
