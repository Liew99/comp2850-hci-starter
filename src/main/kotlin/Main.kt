import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.sessions.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import io.pebbletemplates.pebble.PebbleEngine

import routes.taskRoutes
import routes.configureHealthCheck

import utils.SessionData
import utils.ParticipantCounter
import utils.generateRequestId

import java.io.StringWriter

fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    val host = "0.0.0.0"

    embeddedServer(Netty, port = port, host = host) {
        configureLogging()
        configureTemplating()
        configureSessions()
        configureRouting()
    }.start(wait = true)
}

fun Application.configureLogging() {
    install(CallLogging) {
        format { call ->
            val status = call.response.status()
            val method = call.request.httpMethod.value
            val path = call.request.path()
            "$method $path - $status"
        }
    }
}

val PebbleEngineKey = AttributeKey<PebbleEngine>("PebbleEngine")

fun Application.configureTemplating() {
    val engine =
        PebbleEngine.Builder()
            .loader(io.pebbletemplates.pebble.loader.ClasspathLoader().apply {
                prefix = "templates/"
            })
            .autoEscaping(true)
            .cacheActive(false)
            .strictVariables(false)
            .build()

    attributes.put(PebbleEngineKey, engine)
}

fun Application.configureSessions() {
    install(Sessions) {
        cookie<SessionData>("COMP2850_SESSION") {
            cookie.path = "/"
            cookie.httpOnly = true
            cookie.extensions["SameSite"] = "Strict"
        }
    }
}

fun Application.configureRouting() {

    routing {

        // FIXED: assign participant ID *inside routing*, AFTER sessions plugin is installed
        intercept(ApplicationCallPipeline.Setup) {
            if (call.sessions.get<SessionData>() == null) {
                val next = ParticipantCounter.next()
                call.sessions.set(SessionData("P$next"))
            }
        }

        staticResources("/static", "static")

        configureHealthCheck()

        taskRoutes()
    }
}
