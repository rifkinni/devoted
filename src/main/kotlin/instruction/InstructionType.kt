package instruction

/**
 * A complete list of all valid instructions the database can execute.
 * @param pattern is used for validating the correct syntax and parameters for the instruction.
 */
enum class InstructionType(val pattern: Regex?) {
    SET(Regex("^SET [a-zA-Z0-9]+ [a-zA-Z0-9]+\$")),
    GET(Regex("^GET [a-zA-Z0-9]+\$")),
    DELETE(Regex("^DELETE [a-zA-Z0-9]+\$")),
    COUNT(Regex("^COUNT [a-zA-Z0-9]+\$")),
    BEGIN(Regex("^BEGIN\$")),
    COMMIT(Regex("^COMMIT\$")),
    ROLLBACK(Regex("^ROLLBACK\$")),
    END(Regex("^END\$")),
    ERROR(null);
}
