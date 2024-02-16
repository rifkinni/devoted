package transaction

/**
 * Instances of the CommittedDatabase class manipulate the database state outside any open transactions.
 * @see CommittedDatabase#commit for committing and closing an open transaction
 */
class CommittedDatabase: TransactionState {
    private val storage: MutableMap<String, String?> = mutableMapOf()
    private val count: MutableMap<String, Int> = mutableMapOf()

    override fun set(key: String, value: String?) {
        updateCount(storage[key], -1)
        updateCount(value, 1)
        storage[key] = value
    }

    override fun get(key: String): String? {
        return storage[key]
    }

    override fun delete(key: String) {
        updateCount(storage[key], -1)
        storage.remove(key)
    }

    override fun count(value: String): Int {
        return count[value] ?: 0
    }

    fun commit(openTransaction: OpenTransaction) {
        openTransaction.storage.forEach { (key, value) ->
            if (value == null) delete(key) else set(key, value)
        }
    }

    private fun updateCount(value: String?, increment: Int) {
        if (value != null) {
            count[value] = (count[value] ?: 0) + increment
        }
    }
}
