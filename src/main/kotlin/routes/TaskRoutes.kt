package routes

import data.TaskRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.pebbletemplates.pebble.PebbleEngine
import utils.SessionData
import utils.Logger
import utils.generateRequestId
import utils.ParticipantCounter
import java.io.StringWriter
import kotlin.system.measureTimeMillis

fun Route.taskRoutes() {

    val pebble =
        PebbleEngine.Builder()
            .loader(
                io.pebbletemplates.pebble.loader.ClasspathLoader().apply {
                    prefix = "templates/"
                }
            ).build()

    // Detect HTMX
    fun ApplicationCall.isHtmx(): Boolean =
        request.headers["HX-Request"]?.equals("true", ignoreCase = true) == true

    fun jsMode(call: ApplicationCall): String =
        if (call.isHtmx()) "on" else "off"

    // Auto-create participant sessions
    fun ApplicationCall.sessionId(): String {
        var session = sessions.get<SessionData>()

        if (session == null) {
            val next = ParticipantCounter.next()
            session = SessionData(id = "P$next")
            sessions.set(session)
        }

        return session.id
    }

    // Timing helper
    fun timed(block: () -> Unit): Long = measureTimeMillis(block)

    // ---------------------------------------------------------
    // GET /tasks
    // ---------------------------------------------------------
    get("/tasks") {
        val model = mapOf(
            "title" to "Tasks",
            "tasks" to TaskRepository.all()
        )

        val template = pebble.getTemplate("tasks/index.peb")
        val writer = StringWriter()
        template.evaluate(writer, model)
        call.respondText(writer.toString(), ContentType.Text.Html)
    }

    // ---------------------------------------------------------
    // POST /tasks  (T1_add)
    // ---------------------------------------------------------
    post("/tasks") {
        val sessionId = call.sessionId()
        val reqId = generateRequestId()
        val title = call.receiveParameters()["title"].orEmpty().trim()

        if (title.isBlank()) {
            Logger.validationError(
                sessionId = sessionId,
                requestId = reqId,
                taskCode = "T1_add",
                outcome = "blank_title",
                jsMode = jsMode(call)
            )
            return@post call.respond(HttpStatusCode.BadRequest)
        }

        var newTaskId = -1
        val ms = timed {
            val task = TaskRepository.add(title)
            newTaskId = task.id
        }

        Logger.success(
            sessionId = sessionId,
            requestId = reqId,
            taskCode = "T1_add",
            ms = ms,
            jsMode = jsMode(call)
        )

        call.response.headers.append("Location", "/tasks")
        call.respond(HttpStatusCode.SeeOther)
    }

    // ---------------------------------------------------------
    // POST /tasks/{id}/delete  (T2_delete)
    // ---------------------------------------------------------
    post("/tasks/{id}/delete") {
        val sessionId = call.sessionId()
        val reqId = generateRequestId()

        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            Logger.validationError(
                sessionId = sessionId,
                requestId = reqId,
                taskCode = "T2_delete",
                outcome = "invalid_id",
                jsMode = jsMode(call)
            )
            return@post call.respond(HttpStatusCode.BadRequest)
        }

        val ms = timed {
            TaskRepository.delete(id)
        }

        Logger.success(
            sessionId = sessionId,
            requestId = reqId,
            taskCode = "T2_delete",
            ms = ms,
            jsMode = jsMode(call)
        )

        call.response.headers.append("Location", "/tasks")
        call.respond(HttpStatusCode.SeeOther)
    }

    // ---------------------------------------------------------
    // GET /tasks/search  (T3_filter)
    // ---------------------------------------------------------
    get("/tasks/search") {
        val sessionId = call.sessionId()
        val reqId = generateRequestId()

        val query = call.request.queryParameters["q"]
            ?.trim()
            ?.lowercase()
            .orEmpty()

        lateinit var filtered: List<data.Task>
        val ms = timed {
            filtered = TaskRepository.all()
                .filter { it.title.lowercase().contains(query) }
        }

        Logger.success(
            sessionId = sessionId,
            requestId = reqId,
            taskCode = "T3_filter",
            ms = ms,
            jsMode = jsMode(call)
        )

        val model = mapOf("tasks" to filtered)
        val template = pebble.getTemplate("tasks/_list.peb")
        val writer = StringWriter()
        template.evaluate(writer, model)

        call.respondText(writer.toString(), ContentType.Text.Html)
    }
}
