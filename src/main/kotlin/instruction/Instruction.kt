package instruction

/**
 * The instruction class maps a string input to an executable instruction.
 * If the string input does not meet validation requirements (due to bad syntax, missing parameters, etc),
 * the constructor falls back to the default "ERROR" which exits the program with a non-zero exit code.
 */
class Instruction(input: String) {
    var type: InstructionType
    var args: List<String>

    init {
        val trimmed = input.trim()
        val split = trimmed.split(" ")
        try {
            type = InstructionType.valueOf(split[0])
            if (type.pattern?.matches(trimmed) == true) {
                args = split.drop(1)
            } else { // invalid parameters
                type = InstructionType.ERROR
                args = listOf("126") // exit code "command can not be executed" https://tldp.org/LDP/abs/html/exitcodes.html
            }
        } catch(e: IllegalArgumentException) {
            type = InstructionType.ERROR
            args = listOf("127") // exit code "command not found" https://tldp.org/LDP/abs/html/exitcodes.html
        }
    }
}
