package de.pause

import de.pause.db.configureDatabase
import de.pause.model.PostgresDishRepository
import de.pause.model.UserRepository
import de.pause.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.launch

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val articleRepository = PostgresDishRepository()
    val userRepository = UserRepository()
    configureDatabase()
    launch {
        userRepository.setDefaultPasswordOfPreloadedUsers()
    }
    configureHTTP()
    configureSerialization()
    configureSecurity()
    configureCORS()
    configureRouting(articleRepository, userRepository)
    //configureValidation()
}
