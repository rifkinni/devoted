import instruction.Instruction
import instruction.InstructionType
import transaction.CommittedDatabase
import transaction.OpenTransaction
import transaction.TransactionState
import kotlin.system.exitProcess

/**
 * The TransactionManager is responsible for creating new transactions, committing, and rolling back.
 * Managing the individual transaction state is delegated to the #TransactionState implementations.
 */
class TransactionManager {
    private var committedDatabase = CommittedDatabase()
    private var openTransactions = mutableListOf<OpenTransaction>()
    private var currentDatabase: TransactionState = committedDatabase

    fun execute(instruction: Instruction) {
        when (instruction.type) {
            InstructionType.BEGIN -> begin()
            InstructionType.COMMIT -> commit()
            InstructionType.ROLLBACK -> rollback()
            InstructionType.SET -> currentDatabase.set(instruction.args[0], instruction.args[1])
            InstructionType.GET -> println(currentDatabase.get(instruction.args[0]))
            InstructionType.DELETE -> currentDatabase.delete(instruction.args[0])
            InstructionType.COUNT -> println(currentDatabase.count(instruction.args[0]))
            InstructionType.END -> exitProcess(0)
            InstructionType.ERROR -> exitProcess(instruction.args[0].toInt())
        }
    }

    private fun begin() {
        openTransactions.add(OpenTransaction(currentDatabase))
        currentDatabase = openTransactions.last()
    }

    /**
     * applies to all open transactions
     */
    private fun commit() {
        openTransactions.forEach {
            committedDatabase.commit(it)
        }
        openTransactions = mutableListOf()
    }

    /**
     * applies only to the current open transaction
     */
    private fun rollback() {
        if (openTransactions.size > 0) {
            openTransactions.removeLast()
            currentDatabase = openTransactions.lastOrNull() ?: committedDatabase
        } else {
            println("TRANSACTION NOT FOUND")
        }
    }
}
