package transaction

/**
 * Implementors of the TransactionState interface are responsible for tracking a single database transaction state.
 * The methods #set and #delete write data to the current transaction.
 * The methods #get and #count read data from the database.
 */
interface TransactionState {
    fun set(key: String, value: String?)
    fun get(key: String): String?
    fun delete(key: String)
    fun count(value: String): Int
}
