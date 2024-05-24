package de.pause

import de.pause.model.DatabaseFactory
import de.pause.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureHTTP()
    configureTemplating()
    configureSerialization()
    configureRouting()
    configureValidation()
}
