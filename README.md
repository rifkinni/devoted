# About
This is a Kotlin implementation of an in-memory hash database that supports read/write operations as well as transaction management including atomic commits and rollbacks.

## Performance notes
With no open transactions, read/write performance should be O(log(N)) or better. Note that performance degrades with the number of open transactions. While nested transactions are supported, having too many open transactions is not recommended.

# Running the CLI
```bash
java -jar devoted.jar
```

# Running with example input
```bash
java -jar devoted.jar < examples/example1.txt
```
