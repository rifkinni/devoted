package transaction

/**
 * Instances of an OpenTransaction represent an atomic database transaction that has not yet been committed.
 * @param delegate is the #CommittedDatabase or another #OpenTransaction if this is a nested transaction.
 */
class OpenTransaction(private val delegate: TransactionState): TransactionState {
    val storage: MutableMap<String, String?> = mutableMapOf()
    private val count: MutableMap<String, Int> = mutableMapOf()

    override fun set(key: String, value: String?) {
        updateCount(get(key), -1)
        updateCount(value, 1)
        storage[key] = value
    }

    override fun get(key: String): String? {
        return if (storage.containsKey(key)) {
            storage[key]
        } else {
            delegate.get(key)
        }
    }

    override fun delete(key: String) {
        updateCount(get(key), -1)
        storage[key] = null
    }

    override fun count(value: String): Int {
        return (count[value] ?: 0) + delegate.count(value)
    }

    private fun updateCount(value: String?, increment: Int) {
        if (value != null) {
            count[value] = (count[value] ?: 0) + increment
        }
    }
}
