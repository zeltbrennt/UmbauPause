package de.pause

import de.pause.db.configureDatabase
import de.pause.model.PostgresArticleRepository
import de.pause.plugins.configureHTTP
import de.pause.plugins.configureRouting
import de.pause.plugins.configureSerialization
import de.pause.plugins.configureTemplating
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val articleRepository = PostgresArticleRepository()
    configureDatabase()
    configureHTTP()
    configureTemplating()
    configureSerialization(articleRepository)
    configureRouting()
    //configureValidation()
}
