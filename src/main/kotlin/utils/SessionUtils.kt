package utils

import java.util.UUID

fun generateRequestId(): String =
    UUID.randomUUID().toString()
