package utils

import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * Logger for COMP2850 Week 9 — writes to data/metrics.csv
 *
 * Schema (must match course documentation):
 * ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode
 */
object Logger {

    private val file = File("data/metrics.csv").apply {
        parentFile.mkdirs()
        if (!exists()) {
            writeText("ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode\n")
        }
    }

    @Synchronized
    fun log(
        sessionId: String,
        requestId: String,
        taskCode: String,
        step: String,
        outcome: String,
        ms: Long,
        httpStatus: Int,
        jsMode: String
    ) {
        val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        val row =
            "$timestamp,$sessionId,$requestId,$taskCode,$step,$outcome,$ms,$httpStatus,$jsMode\n"

        file.appendText(row)
    }

    // Convenience helpers EXACTLY as expected for COMP2850

    fun success(
        sessionId: String,
        requestId: String,
        taskCode: String,
        ms: Long,
        jsMode: String
    ) = log(
        sessionId = sessionId,
        requestId = requestId,
        taskCode = taskCode,
        step = "success",
        outcome = "",
        ms = ms,
        httpStatus = 200,
        jsMode = jsMode
    )

    fun validationError(
        sessionId: String,
        requestId: String,
        taskCode: String,
        outcome: String,
        jsMode: String
    ) = log(
        sessionId = sessionId,
        requestId = requestId,
        taskCode = taskCode,
        step = "validation_error",
        outcome = outcome,
        ms = 0,
        httpStatus = 400,
        jsMode = jsMode
    )

    fun fail(
        sessionId: String,
        requestId: String,
        taskCode: String,
        outcome: String,
        jsMode: String
    ) = log(
        sessionId = sessionId,
        requestId = requestId,
        taskCode = taskCode,
        step = "fail",
        outcome = outcome,
        ms = 0,
        httpStatus = 500,
        jsMode = jsMode
    )
}
