import instruction.Instruction

fun main() {
    val manager = TransactionManager()

    while (true) {
        val input = readln()
        val instruction = Instruction(input)
        manager.execute(instruction)
    }
}
