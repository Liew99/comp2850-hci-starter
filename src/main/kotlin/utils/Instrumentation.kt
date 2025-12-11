package utils

/**
 * Utility for measuring execution time of an operation.
 *
 * Example log message:
 * "search took 2ms"
 */
fun timed(label: String, block: () -> Any?): String {
    val start = System.currentTimeMillis()
    block()
    val duration = System.currentTimeMillis() - start
    return "$label took ${duration}ms"
}
